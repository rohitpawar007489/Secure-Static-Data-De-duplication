<%-- 
    Document   : Menu
    Created on : Dec 3, 2014, 10:21:22 AM
    Author     : Rohit
--%>

<%@page import="java.util.Iterator"%>
<%@page import="dedup.db.File"%>
<%@page import="java.util.List"%>
<%@page import="dedup.db.DatabaseManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
<meta name="viewport" content="width=device-width, initial-scale=1.0"> 
  <title> Menu </title>
  <meta name="description" content=" Login Form " />
  <meta name="keywords" content="css3, login, form, custom, input, submit, button, html5, placeholder" />
  <meta name="author" content="Codrops" />
  <link rel="shortcut icon" href="../favicon.ico"> 
  <link rel="stylesheet" type="text/css" href="css/style.css" />
	<script src="js/modernizr.custom.63321.js"></script>
		<!--[if lte IE 7]><style>.main{display:none;} .support-note .note-ie{display:block;}</style><![endif]-->
	<style>	
			@import url(http://fonts.googleapis.com/css?family=Montserrat:400,700|Handlee);
			body {
				background: #eedfcc url(images/bg3.jpg) no-repeat center top;
				-webkit-background-size: cover;
				-moz-background-size: cover;
				background-size: cover;
			}
			.container > header h1,
			.container > header h2 {
				color: #fff;
				text-shadow: 0 1px 1px rgba(0,0,0,0.5);
			}
	</style>
        
</head>
<body>
<div class="container">
	
			
<header>
	<br><br><br><br><br>
	<h1><strong>Welcome to De-duplication Project </strong> </h1>
			
<div class="support-note">
	<span class="note-ie">Sorry, only modern browsers.</span>
</div>
				
</header>
			
<section class="main">
        <div align="right">
            <h3><a href="index.html">Log-out</a></h3>
        </div>

     <br><br><br><br>
  <form name="search" action="/Search" method="post">
            Search File Name: <input type="text" name="filename" size="70">
            <input type="submit" value="Search"/>
        </form>
        
        <br><br><br>
      
	   <form name="download" action="/SeqDownload" method="post" >
            
        
        <%
          // Cookie c[]=request.getCookies();
            /*  to display cookie
           for(int i=0;i<c.length;i++)
           {
               out.println(c[i].getName());             
               out.println(c[i].getValue());
           }*/
    
           HttpSession session1=request.getSession(false);
           int aid=(Integer)session1.getAttribute("aid");//retrieve account id 
           List <String>files=DatabaseManager.displayFiles(aid);
           Iterator<String> iterator=files.iterator();
           
           StringBuilder comboStr=new StringBuilder();
           comboStr.append("<select name=\"filename\">");
           
          //display list of uploaded file in user account 
          while(iterator.hasNext())
          {
              String filename=new String(iterator.next());
              comboStr.append("<option value=\""+filename+"\">"+filename+"</option>");
              
          }
         
          comboStr.append("</select>");
          out.print("List of Uploaded File:");
          out.println(comboStr);
           
        %>
     
           <input type="submit" value="Download">
           <br><br>
           <h3>Note:For Faster Download use Dedup Download Manager</h3>
        </form>

        <br><br><br>
         
	   <form name="delete" action="/Delete1" method="post" >
            
        
        <%
          // Cookie c[]=request.getCookies();
            /*  to display cookie
           for(int i=0;i<c.length;i++)
           {
               out.println(c[i].getName());             
               out.println(c[i].getValue());
           }*/
    
           HttpSession session2=request.getSession(false);
           int aid1=(Integer)session2.getAttribute("aid");//retrieve account id 
           List <String>files1=DatabaseManager.displayFiles(aid1);
           Iterator<String> iterator1=files1.iterator();
           
           StringBuilder comboStr1=new StringBuilder();
           comboStr1.append("<select name=\"filename1\">");
           
          //display list of uploaded file in user account 
          while(iterator1.hasNext())
          {
              String filename1=new String(iterator1.next());
              comboStr1.append("<option value=\""+filename1+"\">"+filename1+"</option>");
              
          }
         
          comboStr1.append("</select>");
          out.print("List of Uploaded File:");
          out.println(comboStr1);
           
        %>
      
           <input type="submit" value="Delete">
        </form>

        <br><br><br>
        
        <form name="upload" action="/Upload" method="post" enctype="multipart/form-data" >
            Upload Files to:
            <input type="radio" name="client" value="Dropbox" >Dropbox
            <input type="radio" name="client" value="LocalDrive" checked="checked">Local Drive<br><br>
             Select File to Upload:<input type="file" name="filename" multiple="ON">
            <input type="submit" value="Upload">
          
        </form>
    
</section>
			
</div>

    </body>
</html>


                 
     