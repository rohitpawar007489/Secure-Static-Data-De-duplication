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
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Rohit
 */
@WebServlet(name = "NewServlet", urlPatterns =
{
    "/Download1"
})
public class NewServlet extends HttpServlet
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
          try
        {
            //PrintWriter pw=response.getWriter();
            //      Cookie []cookies=request.getCookies();
            //    int aid=Integer.parseInt(cookies[1].getValue());
            HttpSession session = request.getSession(false);
            int aid = (Integer)session.getAttribute("aid");
                    
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
            
//            FileOutputStream fos=new FileOutputStream(filename);
            for( Block o:block1)
            {
                byte[] block = DropBoxClient.getBlock(o.getOid());
                byte[] decrypt = Encrypt.decrypt(block,o.getHash());
                //pw.println("<br>Content of block "+new String(block));
                bos.write(decrypt);
            }
            bos.close();
//            pw.println("<br><br><br>Your File Downloaded successully");
//            pw.println("<br><br><a href=\"Menu.jsp\">Download Another File</a>");
//            pw.println("<br><br><a href=\"index.jsp\">Log-out</a>");
//            pw.println("</body></html>");
//            pw.close();

        }
        catch(Exception e)
        {
            e.printStackTrace();
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
        processRequest(request, response);
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
        return "Download servlet1";
    }// </editor-fold>

}
