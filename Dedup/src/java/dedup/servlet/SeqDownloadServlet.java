/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dedup.servlet;

import dedup.db.Block;
import dedup.db.DatabaseManager;
import dedup.security.Encrypt;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Rohit
 */
@WebServlet(name = "SeqDownloadServlet", urlPatterns =
{
    "/SeqDownload"
})
public class SeqDownloadServlet extends HttpServlet
{

        protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
        {
          try
        {
            //PrintWriter pw=response.getWriter();
           // Cookie []cookies=request.getCookies();
            //int aid=Integer.parseInt(cookies[1].getValue());
            HttpSession session = request.getSession(false);
            int aid = (Integer) session.getAttribute("aid");
             System.out.println("Download start time:"+new Date());
 
            String filename=request.getParameter("filename");
            response.addHeader("content-type","application/binary");
            response.addHeader("content-disposition","attachment; filename=\""+filename+"\"");
            OutputStream os=response.getOutputStream();
            BufferedOutputStream bos=new BufferedOutputStream(os);
            //pw.println("<html><body bgcolor=\"orange\" align=\"center\"><br><h1><font color=\"green\"> Downloading file :"+filename+"<br></font></h1>");
         
            int fileid=DatabaseManager.getFileid(filename, aid);
            System.out.println("Fileid:"+fileid);
         
            List<Integer> bid=DatabaseManager.getBid(fileid);
            List<Block> block1 = DatabaseManager.getBlockData(bid);
            Map<String, String> fileMetaData = DatabaseManager.getFileMetaData(fileid);
            String drive = fileMetaData.get("DRIVE");
            long original_file_size=Long.parseLong(fileMetaData.get("SIZE"));
            long check_file_size=0;
//            FileOutputStream fos=new FileOutputStream(filename);
            for( Block o:block1)
            {
                //System.out.println("Retrieving Block:"+o.getBid());
                byte []block=null;
                if(drive.equals("LocalDrive"))
                {
                    block = LocalDriveClient.getBlock(o.getOid());
                }
                else if(drive.equals("Dropbox"))
                {
                    try
                    {
                       block=DropBoxClient.getBlock(o.getOid()); 
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                        System.out.println("Error:While Downloading block from Dropbox");
                    }
                    
                }
                 
                byte[] decrypt = Encrypt.decrypt(block,o.getHash());
                check_file_size+=decrypt.length;
              //  System.out.println("Size of block :"+block.length+" and decrypt:"+ decrypt.length);
                //pw.println("<br>Content of block "+new String(block));
                bos.write(decrypt);
            }
            bos.close();
            System.out.println("Original file Size :"+original_file_size);
            System.out.println("Check File Size:"+check_file_size);
            if(original_file_size==check_file_size)
            {
                System.out.println("No Data Loss while Downloading ");
            }
            else
            {
                System.out.println("Data Lost while Downloading");
            }
//            pw.println("<br><br><br>Your File Downloaded successully");
//            pw.println("<br><br><a href=\"Menu.jsp\">Download Another File</a>");
//            pw.println("<br><br><a href=\"index.jsp\">Log-out</a>");
//            pw.println("</body></html>");
//            pw.close();
              System.out.println("Download end time:"+new Date());


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
       
     }
     @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Sequential Downloading";
    }// </editor-fold>

}
