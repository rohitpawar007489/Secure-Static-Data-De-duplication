/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientapp;

import dedup.db.Block;
import dedup.db.DatabaseManager;
import dedup.security.Integrity;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

/**
 *
 * @author Rohit
 */
public class DownloadClient
{

    JComboBox comboBox;

    DownloadClient(int aid) throws SQLException
    {
        JFrame frame = new JFrame("Combo Box Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new FlowLayout());

        Vector comboBoxItems = new Vector();
        List<String> files = DatabaseManager.displayFiles(aid);
        Iterator<String> iterator = files.iterator();
        while (iterator.hasNext())
        {
            comboBoxItems.add(iterator.next());
        }
        /*  comboBoxItems.add("A");
         comboBoxItems.add("B");
         comboBoxItems.add("C");
         comboBoxItems.add("D");
         comboBoxItems.add("E");*/
        final DefaultComboBoxModel model = new DefaultComboBoxModel(comboBoxItems);
        comboBox = new JComboBox(model);
        frame.add(comboBox);

        JButton button = new JButton("Download");
        frame.add(button);
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                try
                {
                       System.out.println("Download start time :"+new Date());

                    //  model.addElement("F");
                    Object selectedItem = comboBox.getSelectedItem();
                    final String filename = selectedItem.toString();
                    System.out.println("Selected Item is :" + filename);
                    List<Block> blocks = Client.getBlocks(filename);
                    int fileid = DatabaseManager.getFileid(filename);
                    Map<String, String> fileMetaData = DatabaseManager.getFileMetaData(fileid);
                    final String drive=fileMetaData.get("DRIVE");
                    System.out.println("Drive:"+drive);
                    Semaphore downloadSemaphore =new Semaphore(20);
                    for (final Block b : blocks)
                    {
                       // downloadSemaphore.acquire();
                       // new Thread()
                        //{
                          //  public void run()
                            //{
                                try
                                {
                                    byte []buffer=Client.downloadBlock(b,drive);
                                    RandomAccessFile raf=new RandomAccessFile(filename,"rw");
                                    int chunkPos = DatabaseManager.getChunkPos(b.getBid());
                                    raf.seek(chunkPos);
                                    raf.write(buffer);
                                    raf.close();
                                } catch (Exception ex)
                                {
                                    Logger.getLogger(DownloadClient.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            //}
                        //}.start();
                        //downloadSemaphore.release();
                    }
                       System.out.println("Download end time :"+new Date());

                    System.out.println("File downloaded successfully");
                    System.out.println("Checking Integriyt of uploaded and download file");
                    Integrity integrity=new Integrity();
                    integrity.checkIntegrity(filename);
                    
                    /*    FileOutputStream fos=new FileOutputStream(filename);
                     for( Block o:blocks)
                     {
                     //  byte[] block = DropBoxClient.getBlock(o.getOid());
                     byte[] block = LocalDriveClient.getBlock(o.getOid());
                     byte[] decrypt = Encrypt.decrypt(block,o.getHash());
                     //pw.println("<br>Content of block "+new String(block));
                     bos.write(decrypt);
                     }
                     bos.close();*/

                } catch (Exception ex)
                {
                    Logger.getLogger(DownloadClient.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        });

        frame.setVisible(true);
    }

    /*public static void main(String[] args) {  
     new DownloadClient();  
     } */
}
