/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dedup.servlet;

import dedup.db.DatabaseManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;


/**
 *
 * @author Rohit
 */
@MessageDriven(mappedName = "jms/GlassFishDeleteQueue", activationConfig =
{
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class DeleteMessageBean implements MessageListener
{
   
     
    public DeleteMessageBean()
    {
    }
    
    @Override
    public void onMessage(Message message)
    {
        if(message instanceof TextMessage)
        {
            try {
                //  System.out.println("Received file to be deleted ");
                TextMessage txt=(TextMessage)message;
                String filename = txt.getText();
                
                System.out.println("Delete Queue I received a filename :"+filename+" at "+new Date());
                
            int fileid=DatabaseManager.getFileid(filename);
            //delete entry from filechunk ,filemetadata and file is compulsary
            List<Integer> bid = DatabaseManager.getBid(fileid);
            Map<String, String> fileMetaData = DatabaseManager.getFileMetaData(fileid);
            final String drive = fileMetaData.get("DRIVE");
            
            DatabaseManager.deleteFileChunk(fileid);
        //    Semaphore  deleteSemaphore  =new Semaphore(20);
            for(final Integer b:bid)
            {
                    int blockCount = DatabaseManager.getBlockCount(b);
                    System.out.println("Block count  "+blockCount +" for bid "+b);
                    if(blockCount==1)   //then delete entry from block
                    {
                        String oid = DatabaseManager.getOid(b);
                        DatabaseManager.deleteBlock(b);
                        //block has no longer use so delete block from cloud storage as well
                        //retrive oid from block by providing bid then delete
                        //local drive client
                        if(drive.equals("LocalDrive"))
                        {
                            boolean deleteBlock = LocalDriveClient.deleteBlock(oid);
                            if(!deleteBlock)
                            {
                                System.out.println("Failed to delete block from LocalDrive with oid:"+oid);                                
                            }
                        }
                        if(drive.equals("Dropbox"))
                        {
                            boolean deleteBlock = DropBoxClient.deleteBlock(oid);
                            if(!deleteBlock)
                            {
                                System.out.println("Failed to delete block from Dropbox with oid:"+oid);
                            }
                        }
          
                    }
                    else    //decrement count by 1 bcz that block used by another user
                    {
                        blockCount=blockCount-1;
                        DatabaseManager.updateBlockCount(b, blockCount);
                    }


            }
              DatabaseManager.deleteFileMetaData(fileid);
              DatabaseManager.deleteFile(fileid);
              System.out.println("Delete end time :"+new Date());

            } catch (JMSException ex) {
                Logger.getLogger(DeleteMessageBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex)
            {
                Logger.getLogger(DeleteMessageBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex)
            {
                Logger.getLogger(DeleteMessageBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
}
