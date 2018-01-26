/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dedup.servlet;

import dedup.db.Block;
import dedup.security.Encrypt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Rohit
 */
@WebServlet(name = "ClientDownloadServlet", urlPatterns =
{
    "/ClientDownload"
})
public class ClientDownloadServlet extends HttpServlet implements Runnable
{

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        try
        {
            
            InputStream is=request.getInputStream();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            byte []buffer=new byte[10240];
            int n;
            while((n=is.read(buffer))!=-1)
            {
                baos.write(buffer,0,n);
            }
            byte []bytes=baos.toByteArray();
            baos.close();
            
        //    System.out.println(Arrays.toString(bytes));
            
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            Block o = (Block) ois.readObject();
         //   String drive=(String)ois.readObject();
            
            String drive = request.getParameter("drive");
            System.out.println("Received Drive :"+drive);
          byte[] block = null;
          
            if(drive.equals("LocalDrive"))
            {
                   block = LocalDriveClient.getBlock(o.getOid());
//      
            }
            if(drive.equals("Dropbox"))
            {
                   block = DropBoxClient.getBlock(o.getOid());

            }
             byte[] decrypt = Encrypt.decrypt(block, o.getHash());

              response.setContentLength(decrypt.length);
            ObjectOutputStream os=new ObjectOutputStream(response.getOutputStream());
            os.writeInt(decrypt.length);
            os.write(decrypt);
            os.flush();
            os.close();
            
        } catch (Exception ex)
        {
            Logger.getLogger(ClientDownloadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
//        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
//    @Override
    protected void doPost2(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
//        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>

    @Override
    public void run()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
