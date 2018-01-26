/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dedup.servlet;

import dedup.db.DatabaseManager;
import dedup.db.User;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Om
 */
@WebServlet(name = "NewUserServlet", urlPatterns = {"/NewUser"})
public class NewUserServlet extends HttpServlet {

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
            throws ServletException, IOException {
        
        try
        {
            //Get information from client
            //int aid=DatabaseManager.getAid(request.getParameter("acc_name"));
            int aid=Integer.parseInt(request.getParameter("acc_id"));
            boolean accountExist = DatabaseManager.isAccountExist(aid);
          //  System.out.println("acc:"+accountExist);
            if(!accountExist) //account not exist
            {
                //System.out.println("errrrrr");
                response.sendRedirect("FailNewUser.html");
            }
            else    //account exist then create new user
            {
                        //Initialize User Data
                   User user=new User();

                   user.setUsername(request.getParameter("user_name"));
                   user.setPassword( request.getParameter("password"));
                   user.setAid(aid);
                   user.setUser_type("user");

                   int userid=DatabaseManager.addUser(user);

                   if(userid!=-1)
                   {
                       response.sendRedirect("SuccessNewUser.html");
                   }
                   else
                   {
                       response.sendRedirect("FailNewUser.html");
                   }

            }
           
        }
        catch(SQLException e)
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
    public String getServletInfo() {
        return "Servlet to Create New User";
    }// </editor-fold>

}
