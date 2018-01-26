/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dedup.servlet;

import dedup.db.DatabaseManager;
import dedup.db.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
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
@WebServlet(name = "ShowLogServlet", urlPatterns =
{
    "/ShowLog"
})
public class ShowLogServlet extends HttpServlet
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
          
    {
        try
        {
               response.setContentType("text/html;charset=UTF-8");
            PrintWriter pw = response.getWriter();
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
"				<li><a href=\"index.html\">Log-out</a></li>\n" +
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
"						<div class=\"post\">\n" +
"						<h2 class=\"title\">Log Of Uploaded Files </h2>\n" +
"						<div class=\"entry\">\n" +
"   <table>"+
"   <tr><td>Sr.No.</td><td>FileName</td><td>Uploaded by</td><td> Date & Time</td></tr>"+
""                    );
            HttpSession session = request.getSession(false);
            int aid = (Integer)session.getAttribute("aid");
            String username =(String) session.getAttribute("username");
      
            List<Integer> fileid = DatabaseManager.getFileid(aid);
            Iterator<Integer> iterator = fileid.iterator();
            
            //Display File Name
            int i=1;
            while(iterator.hasNext())
            {
               int fid= iterator.next();
                 pw.println("<tr>");
              //   pw.println("<td>"+file.getFileid()+"</td>");
                 pw.println("<td>"+ i+"</td>");
                 pw.println("<td>"+DatabaseManager.getFileName(fid)+"</td>");
                 pw.println("<td>"+username+"</td>");
                 Map<String, String> fileMetaData = DatabaseManager.getFileMetaData(fid);
                
                 pw.println("<td>"+fileMetaData.get("CREATEDATE")+"</td>");
                 pw.println("</tr>");
                 i++;
            }
                    
pw.println("	</table>					</div>\n" +
"					</div>\n" +
"						<div style=\"clear: both;\">&nbsp;</div>\n" +
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
"								   <form name=\"delete\" action=\"/Delete1\" method=\"post\" >\n" +
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
        catch(Exception e)
        {
            System.out.println(e);
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
        return "Short description";
    }// </editor-fold>

}
