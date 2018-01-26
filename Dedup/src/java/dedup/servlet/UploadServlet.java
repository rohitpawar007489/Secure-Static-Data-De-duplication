/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dedup.servlet;

import dedup.chunk.TTTD;
import dedup.db.Block;
import dedup.db.DatabaseManager;
import dedup.db.File;
import dedup.db.FileChunk;
import dedup.hash.Hash;
import dedup.security.Encrypt;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.UploadBean;
import javazoom.upload.UploadException;
import javazoom.upload.UploadFile;
import org.syncany.chunk.Chunk;

/**
 *
 * @author Om
 */
@WebServlet(name = "UploadServlet", urlPatterns =
{
    "/Upload"
})
public class UploadServlet extends HttpServlet
{

    @Resource(mappedName = "jms/GlassFishBookConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/GlassFishBookQueue")
    private Queue queue;
    private static int numberOfBlock,existCount,notExistCount;
 // @Resource(mappedName = "jms/GlassFishBookConnectionFactory")
    //private  ConnectionFactory connectionFactory1;
    //@Resource(mappedName = "jms/GlassFishBlockCountQueue")
    //private  Queue queue1;
    //MessageProducer messageProducer1;

    public void consumeMessages() throws JMSException
    {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer=session.createConsumer(queue);

         try
        {
                
           connection.start();
           while(true)
           {
              // Message message=consumer.receive(1);
               Message message=consumer.receiveNoWait();
               if(message!=null)
               {
               if (message instanceof StreamMessage)
               {
                Semaphore sem=new Semaphore(1);
                sem.acquire();
               // System.out.println("Queue: I received a StreamMessage at " + new Date());
                StreamMessage msg = (StreamMessage) message;
                int fileid = msg.readInt();
                int pos = msg.readInt();
                int length = msg.readInt();
                int textLen = msg.readInt();

                byte[] bytes = new byte[textLen];
                msg.readBytes(bytes);
                String hashVal = Hash.getHashValue(bytes).toString();
               // System.out.println("Received length:" + length);
        //        Thread.sleep(10);
           //     boolean exist = DatabaseManager.isBlockExist(hashVal, length);
                boolean exist = DatabaseManager.isBlockExist(hashVal);
                boolean uploadBlockStatus = false;
               // System.out.println("block exist status:" + exist);
                int bid = -1;
                int encrypt_leng = 0;
            
                if (exist)  //Block already exist
                {
                   // bid = DatabaseManager.getBid(length, hashVal); 
                  //  Semaphore blockexist =new Semaphore(1);
                    //blockexist.acquire();
                    bid=DatabaseManager.getBid(hashVal);
                    // if block already exist then getBlockCount and update block count by one
                    
                    int count=  DatabaseManager.getBlockCount(bid);
                    System.out.println("bid :"+bid+" and count before update:"+count);
                    count=count+1;
                    boolean updateBlockCount = DatabaseManager.updateBlockCount(bid,count);
                    count=  DatabaseManager.getBlockCount(bid);
                    System.out.println("bid :"+bid+" and count after update:"+count);
                    //blockexist.release();
                    if(!updateBlockCount)
                        System.out.println("Failed to update block count into database");
                    
                    uploadBlockStatus = true;
                    System.out.println("Block already exist");
//                    System.out.println("Block Count updated successfully");
                    existCount++;
                    System.out.println("Number of block exist:"+existCount +" out of  "+numberOfBlock);
                   
                } else    //New Block then Upload 
                {
                   // Semaphore semNotExist=new Semaphore(1);
                    //semNotExist.acquire();
                    byte[] encrypt = Encrypt.encrypt(bytes, hashVal, length);
                    String oid = DatabaseManager.createNextOID();
                    //   Map<String, String> fileMetaData = DatabaseManager.getFileMetaData(fileid);
                    Map<String, String> fileMetaData = DatabaseManager.getFileMetaData(fileid);
                    String drive = fileMetaData.get("DRIVE");
              //      System.out.println("Fileid:"+fileid+" Drive:"+drive);

                    //Local Client
                    if(drive.equals("LocalDrive"))
                    {
                        uploadBlockStatus = LocalDriveClient.uploadBlock(oid, encrypt, encrypt.length);
                    }
                    //Dropbox client
                    else if(drive.equals("Dropbox"))
                    {
                        Semaphore ds=new Semaphore(1);
                        ds.acquire();
                        uploadBlockStatus = DropBoxClient.uploadBlock(oid, encrypt, encrypt.length);
                        ds.release();                   
                    }
                    
                    //send upload block status to upload servlet via AckQueue
                  /*  System.out.println("Status of uploaded block:" + uploadBlockStatus);
                    
                        Connection connection = connectionFactory.createConnection();
                        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                        // Semaphore producerSemaphore=new Semaphore(1);
                        //producerSemaphore.acquire();
                        MessageProducer messageProducer = session.createProducer(queue);
                    
                    StreamMessage sm = session.createStreamMessage();
                    sm.writeInt(fileid);
                    sm.writeInt(pos);
                    sm.writeBoolean(uploadBlockStatus);
                    System.out.println("Sending Ack Stream Msg...");
                    messageProducer.send(sm);
                  
                                //      producerSemaphore.release();

                    System.out.println("Stream Ack Msg Sent...");
                    messageProducer.close();
                    session.close();
                    connection.close();
                    
*/
               //     if (uploadBlockStatus)
                 //   {
                     //   Block block = new Block(oid, length, hashVal);
                        hashVal.trim();
                        Block block = new Block(oid, length, hashVal,1);

                        bid = DatabaseManager.addBlock(block);
                      //  semNotExist.release();
                        notExistCount++;
                   // } else
                    //{
                      //  System.out.println("Failed to upload block");
                    //}

                }
                //if (uploadBlockStatus)
                //{
                //Semaphore semFileChunk=new Semaphore(1);
                //semFileChunk.acquire();
                    FileChunk fc = new FileChunk(fileid, pos, length, bid);
                    DatabaseManager.addFileChunk(fc);
                    sem.release();
                  //  semFileChunk.release();
                 //   pos += length;

                //}
                if((existCount+notExistCount)==numberOfBlock)
                {
                    System.out.println("Number of block exist:"+existCount);
                    System.out.println("Number of block not exist:"+notExistCount);
                    System.out.println("Total number of block:"+numberOfBlock);
                    numberOfBlock=existCount=notExistCount=0;
                } 
              //  Thread.sleep(20);
  			//System.out.println("Message is : " + msg.getText());
            } else if (message instanceof ObjectMessage)
            {
                System.out.println("Queue: I received an ObjectMessage " + " at " + new Date());
                /*				ObjectMessage msg = (ObjectMessage) message;
                 Employee employee = (Employee) msg.getObject();
                 System.out.println("Employee Details: ");
                 System.out.println(employee.getId());
                 System.out.println(employee.getName());
                 System.out.println(employee.getDesignation());
                 System.out.println(employee.getSalary());*/
            } else if (message instanceof TextMessage)
            {
                TextMessage txt = (TextMessage) message;
                String text = txt.getText();
            //    numberOfBlock=0;
                numberOfBlock=Integer.parseInt(text);
                System.out.println("Received number of chunks:" + text);

           /*     //send number of blocks to AckQueue
                Connection connection = connectionFactory.createConnection();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Semaphore producerSemaphore = new Semaphore(1);
                producerSemaphore.acquire();
                MessageProducer messageProducer = session.createProducer(queue);
                TextMessage txt1 = session.createTextMessage();
                txt1.setText(text);
                System.out.println("Sending Number of block ...");
                messageProducer.send(txt1);
                producerSemaphore.release();
                System.out.println("Number block  Sent...");*/
            }


               }
               else
                   break;
           }
          
        } catch (Exception e)
        {
            e.printStackTrace();
        }
         finally
         {
             consumer.close();
             session.close();
             connection.close();
         }

    }
    public void produceMessages(int fileid, int pos, int size, byte[] text) throws InterruptedException, JMSException
    {
                 Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    
        MessageProducer messageProducer = null;
//    TextMessage textFileid;
        //  TextMessage textPos;
        // TextMessage textContent;
        StreamMessage sm;
        try
        {
           Semaphore producerSemaphore = new Semaphore(1);
            producerSemaphore.acquire();
            messageProducer = session.createProducer(queue);

     // textFileid = session.createTextMessage();
            //textPos = session.createTextMessage();
            //textContent = session.createTextMessage();
            sm = session.createStreamMessage();
            sm.writeInt(fileid);
            sm.writeInt(pos);
            sm.writeInt(size);
            sm.writeInt(text.length);
            sm.writeBytes(text);
           // System.out.println("Sending Stream Msg...");
            messageProducer.send(sm);
            messageProducer.send(session.createMessage());
       //     messageProducer.send(session.createMessage());
            producerSemaphore.release();

         //   System.out.println("Stream Msg Sent...");
      //textFileid.setText(""+textFileid);
            //System.out.println("Sending the following message: "+ textFileid.getText());
            //messageProducer.send(textFileid);

      //textPos.setText(""+textPos);
            //System.out.println("Sending the following message: " + textPos.getText());
            //messageProducer.send(textPos);
      //textContent.setText(text.toString());
            //System.out.println("Sending the following message: " + textContent.getText());
            // messageProducer.send(textContent);
            /* //2. Sending ObjectMessage to the Queue
             ObjectMessage objMsg = session.createObjectMessage();
             Employee employee = new Employee(1,"rohit","student",15000);
             objMsg.setObject(employee);                     
             messageProducer.send(objMsg);
             System.out.println("2. Sent ObjectMessage to the Queue");*/
            messageProducer.close();
            session.close();
            connection.close();
        } catch (JMSException e)
        {
            e.printStackTrace();
        }
        finally
        {
            messageProducer.close();
            session.close();
            connection.close();
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {

        try
        {
//            PolarMessageBean.failureCount=PolarMessageBean.numberOfBlock=PolarMessageBean.successCount=0;
//            ReceiverMessageBean.existCount=ReceiverMessageBean.notExistCount=ReceiverMessageBean.numberOfBlock=0;
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter pw = response.getWriter();
            //get aid 
            //     Cookie c[] = request.getCookies();
            //   int aid = Integer.parseInt(c[1].getValue());

            HttpSession session = request.getSession(false);
            int aid = (Integer) session.getAttribute("aid");
            MultipartFormDataRequest multipleRequest = new MultipartFormDataRequest(request);
            UploadBean upb = new UploadBean();
            upb.setFolderstore("e:/project/temp_storage");
            upb.setOverwrite(false);
            upb.store(multipleRequest);

            pw.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
"<head>\n" +
"<meta name=\"keywords\" content=\"\" />\n" +
"<meta name=\"description\" content=\"\" />\n" +
"<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\n" +
"<title>Data De-Duplication</title>\n" +
"<link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />\n" +
"</head>\n" +
"<body>\n" +
"<div id=\"wrapper\">\n" +
"	<div id=\"header\">\n" +
"		<div id=\"logo\">\n" +
"			<h1>Data De-Duplication</h1>\n" +
"			\n" +
"		</div>\n" +
"		\n" +
"	</div>\n" +
"	<!-- end #header -->\n" +
"	<div id=\"menu\">\n" +
"		<ul>\n" +
"		\n" +
"			\n" +
"			\n" +
"			<li><a href=\"index.html\">Log-out</a></li>\n" +
"                        <li><a href=\"/AboutUs\">About Us</a></li>\n" +
"                        <li><a href=\"/Sponsor\">Sponsor</a></li>\n" +
"                        <li><a href=\"/PaperPublication\">Published Paper</a></li>\n" +
"                        <li class=\"current_page_item\"><a href=\"Menu.jsp\">Home</a></li>\n" +
"		</ul>\n" +
"	</div>\n" +
"	<!-- end #menu -->\n" +
"	<div id=\"page\">\n" +
"		<div id=\"page-bgtop\">\n" +
"			<div id=\"page-bgbtm\">\n" +
"				<div id=\"content\">\n" +
"					<div class=\"post\">\n" +
"						<h2 class=\"title\">Upload File </h2>\n" +
"						<div class=\"entry\">\n" +
"							<p>\n" +
"");

//            String parameter = request.getParameter("client");
            String drive=multipleRequest.getParameter("client");
            System.out.println("drive:" + drive);
            Hashtable ht = multipleRequest.getFiles();
            Enumeration e = ht.elements();
            while (e.hasMoreElements()) //for multiple file
            {
                UploadFile f1 = (UploadFile) e.nextElement();

                //Initialize file object
                File file = new File(f1.getFileName(), aid);

                //insert file detail in table
                int fileid = 0;
                try
                {
                   fileid = DatabaseManager.addFile(file); 
                }
                catch(SQLException sqlexception)
                {
                        pw.println("<script type=\"text/javascript\">");
                        pw.println("alert('Filename Already Exist ');");
                        pw.println("</script>");
                        break;
//                        return;
                      //  response.sendRedirect("Menu.jsp");
                }
                
                if (fileid == -1)
                {
                    pw.println("Failed to Add " + f1.getFileName() + "  File");
                }

                pw.println("File name is:" + f1.getFileName() + " <br>Type of file is:" + f1.getContentType() + "<br><br>");

                //Add information to FileMetaData
                Map<String, String> metadata = new HashMap<String, String>();
                metadata.put("CREATEDATE", new Date().toString());
                metadata.put("SIZE", String.valueOf(f1.getFileSize()));
                long original_file_size=f1.getFileSize();
                long check_total_size=0;
//                String parameter = request.getParameter("client");
                //              System.out.println("drive:"+parameter);
                metadata.put("DRIVE", drive);
                metadata.put("TYPE", f1.getContentType());
                //String parameter = request.getParameter("client");

                //            System.out.println("Client:"+parameter);
                boolean addFileMetaData = DatabaseManager.addFileMetaData(fileid, metadata);

                if (!addFileMetaData)
                {
                    pw.println("Failed to add File Meta Data  for file:" + f1.getFileName());
                }

                // to create chunk
                List<Chunk> chunk = TTTD.getChunk(f1.getInpuStream());

                pw.println("Total number of Chunks:" + chunk.size());
                pw.println("<br>Upload start time:"+new Date());

                //  String totalChunk=""+chunk.size();
                Connection connection = connectionFactory.createConnection();
                Session session1 = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Semaphore producerSemaphore = new Semaphore(1);
                producerSemaphore.acquire();
                MessageProducer messageProducer = session1.createProducer(queue);
                producerSemaphore.release();
                TextMessage totalChunk = session1.createTextMessage();
                totalChunk.setText("" + chunk.size());
                messageProducer.send(totalChunk);
                messageProducer.send(session1.createMessage());
               // messageProducer.send(session1.createMessage());
                
                messageProducer.close();
                session1.close();
                connection.close();

             //   System.out.println("fdfdf"+totalChunk);
                //Send total number of Block into BlockCountQueue
              /*  Connection connection1 = connectionFactory.createConnection();
                 Session session1 = connection1.createSession(false,Session.AUTO_ACKNOWLEDGE);
                 messageProducer1 = session1.createProducer(queue1);
                
                 StreamMessage msg=session1.createStreamMessage();
                 msg.writeInt(chunk.size());
                 messageProducer1.send(msg);
                     
    
                 messageProducer1.close();
                 session1.close();
                 connection1.close();*/
                //Traverse chunked element
                Iterator iterator = chunk.iterator();
                int pos = 0;
                int total=chunk.size();
                for(int i=0;i<total;i++)
                //while (iterator.hasNext())
                {
                    
                    //print chunked block
                    if(iterator.hasNext())
                    {
                         Chunk c1 = (Chunk) iterator.next();
                    final byte[] content = c1.getContent();
                   // pw.println("<font color=\"black\"><h2>Contet of chunks are:</h2><br>" + new String(content));
                        System.out.println("Uploading Chunk:"+(i+1)+"out of "+total);
                        String msg="Uploading chunk "+(i+1)+" out of "+total;
                 //       pw.println("<script type=\"text/javascript\">");
                      //  pw.println("alert('"+msg+"');");
                   //     pw.println("</script>");
                        
                        //new ProgressBar.progress(i+1, total);
                       // new ProgressBar().progress(i+1, total);
                    //Get Hash value for chunked block
                    String hashVal = Hash.getHashValue(content).toString();
                        System.out.println("Hash Value of Chunk"+(i+1)+" :"+hashVal);
                        
                    //copy hash val into array
                  //  pw.println("<br><br><h2>Hash Value of Chunk is :</h2>" + hashVal + " Size :" + hashVal.length() + "</font>");
                    /*              final int chunksize = c1.getSize();

                     boolean exist = DatabaseManager.isBlockExist(hashVal, chunksize);
                     int bid = -1;
                     if (exist)  //Block already exist
                     {
                     bid = DatabaseManager.getBid(chunksize, hashVal);
                     pw.println("Block already exist");
                     }
                     else    //New Block then Upload 
                     {
                     produceMessages(fileid, pos, c1.getSize(), content);
                        
                     }
                     FileChunk fc = new FileChunk(fileid, pos, chunksize, bid);
                     DatabaseManager.addFileChunk(fc);
                     pos += chunksize;
                     }*/
                    produceMessages(fileid, pos, c1.getSize(), content);
                //    System.out.println("Content size" + content.length);
                 //   System.out.println("Chunk size" + c1.getSize());
                    pos += c1.getSize(); 
                    check_total_size=pos;
                    consumeMessages();
                    }
                  
                }
              //  System.out.println("File size :"+original_file_size);
              //  System.out.println("Pos size:"+check_total_size);
                if(original_file_size==check_total_size)
                {
                    System.out.println("Chunking Process Done Successfully");
                }
                else
                {
                    System.out.println("Data Loss While Chunking");
                }
             //   System.out.println("\nStart Consuming");
               // consumeMessages();
                pw.println("<br>Upload End Time:"+new Date());
                pw.println("<br><br><br>Your File is  Saved");
                pw.println("                                                        </p>\n" +
"						</div>\n" +
"					</div>\n" +
"					<div style=\"clear: both;\">&nbsp;</div>\n" +
"				</div>\n" +
"				<!-- end #content -->\n" +
"				<div id=\"sidebar\">\n" +
"					<ul>\n" +
"						<li>\n" +
"						  <form name=\"search\" action=\"/Search\" method=\"post\">\n" +
"         Search File: <input type=\"text\" name=\"filename\" />\n" +
"   <!-- <button type=\"submit\" style=\"height:50px ;width:50px;\"><img src=\"images/search.jpg\" alt=\"Submit\"></button> -->\n" +
"   <input type=\"submit\" id=\"search-submit\" value=\"GO\" src/>\n" +
"\n"+
"        </form>\n" +
"\n" +
"						</li>\n" +
"<br>\n" +
"<li>\n" +
"							<h2>Upload</h2>\n" +
"							   <form name=\"upload\" action=\"/Upload\" method=\"post\" enctype=\"multipart/form-data\" >\n" +
"            Upload Files to:\n" +
"            <input type=\"radio\" name=\"client\" value=\"Dropbox\" >Dropbox\n" +
"            <input type=\"radio\" name=\"client\" value=\"LocalDrive\" checked=\"checked\">Local Drive<br><br>\n" +
"            <input type=\"file\" name=\"filename\" multiple=\"ON\">\n" +
"            <input type=\"submit\" value=\"Upload\">\n" +
"          \n" +
"        </form>\n" +
"    \n" +
"\n" +
"						</li>\n" +
"    <br>\n" +
"						<li>\n" +
"							<h2>Download</h2>\n" +
"						   <form name=\"download\" action=\"/SeqDownload\" method=\"post\" >\n" +
"            \n" +
"        \n" +
"");
                    
           HttpSession session2=request.getSession(false);
           int aid2=(Integer)session2.getAttribute("aid");//retrieve account id 
           List <String>files2=DatabaseManager.displayFiles(aid2);
           Iterator<String> iterator2=files2.iterator();
           
           StringBuilder comboStr2=new StringBuilder();
           comboStr2.append("<select name=\"filename\">");
           
          //display list of uploaded file in user account 
          while(iterator2.hasNext())
          {
              String filename2=new String(iterator2.next());
              comboStr2.append("<option value=\""+filename2+"\">"+filename2+"</option>");
              
          }
         
          
          comboStr2.append("</select>");
          pw.print("List of Uploaded File:");
          pw.println(comboStr2);
          pw.println("           <input type=\"submit\" value=\"Download\">\n" +
"           \n" +
"          \n" +
"        </form>\n" +
"\n" +
"						</li>\n" +
"        <br>\n" +
"						<li>\n" +
"							<h2>Delete</h2>\n" +
"								   <form name=\"delete\" action=\"/Delete\" method=\"post\" >\n" +
"            \n" +
"        \n" +
"");

                     HttpSession session3=request.getSession(false);
           int aid3=(Integer)session3.getAttribute("aid");//retrieve account id 
           List <String>files3=DatabaseManager.displayFiles(aid3);
           Iterator<String> iterator3=files3.iterator();
           
           StringBuilder comboStr3=new StringBuilder();
           comboStr3.append("<select name=\"filename1\">");
           
          //display list of uploaded file in user account 
          while(iterator3.hasNext())
          {
              String filename3=new String(iterator3.next());
              comboStr3.append("<option value=\""+filename3+"\">"+filename3+"</option>");
              
          }
         
          comboStr3.append("</select>");
          pw.print("List of Uploaded File:");
          pw.println(comboStr3);
          pw.println("      \n" +
"           <input type=\"submit\" value=\"Delete\">\n" +
"        </form>\n" +
"\n" +
"\n" +
"						</li>\n" +
"					</ul>\n" +
"				</div>\n" +
"				<!-- end #sidebar -->\n" +
"				<div style=\"clear: both;\">&nbsp;</div>\n" +
"			</div>\n" +
"		</div>\n" +
"	</div>\n" +
"	<!-- end #page -->\n" +
"</div>\n" +
"<div id=\"footer\">\n" +
"	<p>Department Of Computer Engineering ,Sinhgad College Of Engineering,Pune.</p>\n" +
"</div>\n" +
"<!-- end #footer -->\n" +
"</body>\n" +
"</html>\n" +
"");
          
                pw.close();

            }
        } catch (UploadException ex)
        {
            Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex)
        {
            Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Servlet to upload file";
    }// </editor-fold> 

}
