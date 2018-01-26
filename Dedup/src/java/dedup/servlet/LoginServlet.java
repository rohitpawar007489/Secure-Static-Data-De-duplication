/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dedup.servlet;

import dedup.db.DatabaseManager;
import java.io.IOException;
import java.sql.SQLException;
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
@WebServlet(name = "LoginServlet", urlPatterns =
{
    "/Login"
})
public class LoginServlet extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
//        System.out.println("LoginServlet.doPost()");
        try
        {

            //read request parameter from client
            String accname = request.getParameter("acc_name");
            String username = request.getParameter("user_name");
            String password = request.getParameter("password");

            boolean result=DatabaseManager.validateLogin(accname, username, password);
            if(result)
            {
                int  aid= DatabaseManager.getAid(accname);
                
                //add cookies
            /*    Cookie aidCookie =new Cookie("aid",""+aid);
                Cookie anameCookie=new Cookie(accname, accname);
                Cookie userCookie=new Cookie(username, username);

                response.addCookie(aidCookie);
                response.addCookie(anameCookie);
                response.addCookie(userCookie);
                */
                HttpSession session=request.getSession(true);
                session.setAttribute("aid", aid);
                session.setAttribute("username", username);
                session.setAttribute("accname", accname);
                //Transfer control to Menu.jsp
                String userType = DatabaseManager.getUserType(aid, username);
                if(userType.equals("user"))
                {
                    response.sendRedirect("Menu.jsp");
                }
                else
                {
                    response.sendRedirect("AdminMenu.jsp");
                }
                
            }
            else
            {
                response.sendRedirect("index.html");
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
    public String getServletInfo()
    {
        return "Servlet to perform login authentication";
    }// </editor-fold>

}
