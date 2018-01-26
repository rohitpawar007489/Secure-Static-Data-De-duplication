/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dedup.servlet;

import dedup.db.Account;
import dedup.db.DatabaseManager;
import dedup.db.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Om
 */
@WebServlet(name = "NewAccountServlet", urlPatterns = {"/NewAccount"})
public class NewAccountServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
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

            //read request parameter from client
            
            //Set Account Name
            
            Account account = new Account();
            String accname=request.getParameter("acc_name");
            account.setName(accname);
            int aid=DatabaseManager.addAccount(account);
        
            if(aid==-1)
            {
                response.sendRedirect("FailNewAccount.html");
            }
            
            //Set User Details
            User user=new User();         
            user.setUsername(request.getParameter("user_name"));
            user.setPassword( request.getParameter("password"));
            user.setAid(aid);
            user.setUser_type("admin");
            int userid = DatabaseManager.addUser(user);
            
            if(aid!=-1 && userid!=-1)
            {
                RequestDispatcher dispatcher = request.getRequestDispatcher("SuccessNewAccount.jsp");
                request.setAttribute("aid", aid); // set aid value in the attribute                
                dispatcher.forward( request, response );
               // response.sendRedirect("SuccessNewAccount.html");
            }
            else
            {
                response.sendRedirect("FailNewAccount.html");
            }
        }
        catch (IOException e)
        {          
            e.printStackTrace();
        } catch (SQLException e) {
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
        return "Servlet to Create New Account";
    }// </editor-fold>

}
