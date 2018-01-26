<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="dedup.db.DatabaseManager"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!--
Design by Free CSS Templates
http://www.freecsstemplates.org
Released for free under a Creative Commons Attribution 2.5 License

Name       : Neatness  
Description: A two-column, fixed-width design with dark color scheme.
Version    : 1.0
Released   : 20100703

-->
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>Data De-Duplication</title>
<link href="style.css" rel="stylesheet" type="text/css" media="screen" />
</head>
<body>
<div id="wrapper">
	<div id="header">
		<div id="logo">
			<h1>Data De-Duplication</h1>
			
		</div>
		
	</div>
	<!-- end #header -->
	<div id="menu">
		<ul>
		
			
			
			<li><a href="index.html">Log-out</a></li>
                        <li><a href="/ShowLog">Show Log</a></li>
                        <li><a href="/AboutUs">About Us</a></li>
                        <li><a href="/Sponsor">Sponsor</a></li>
                        <li><a href="/PaperPublication">Published Paper</a></li>
                        <li class="current_page_item"><a href="Menu.jsp">Home</a></li>
		</ul>
	</div>
	<!-- end #menu -->
	<div id="page">
		<div id="page-bgtop">
			<div id="page-bgbtm">
				<div id="content">
					<div class="post">
						<h2 class="title"><a href="#">Welcome to Data De-Duplication </a></h2>
						<div class="entry">
							<p>
                                                           Data de-duplication is a technique used to improve storage efficiency. In static data de-duplication system, Hashing is carried out at client side. Firstly hashing is done at file level. The de-duplicator identifies duplication by comparing existing hash values in metadata server. If match is found, then logical pointers are created for storing redundant data. If match doesn't exist, then same process is carried out at chunk level. Duplicated data chunks are identified and only one replica of the data is stored in storage. Logical pointers are created for other copies, instead of storing redundant data. If it is a new hash value, it will be recorded in metadata server and the file or corresponding chunk will be stored in file server and its logical path in terms of logical pointers is also stored in metadata server. Basically static de-duplicator is implemented with three components: interface, de-duplicator and storage. Interface carries out hashing of uploaded file and interfaces client with de-duplicator. After receiving hash value, de-duplicator carries out its function as mentioned above. The last component storage consists of file server and meta-data server. Thus, de-duplication can reduce both storage space and network bandwidth.    
                                                        </p>
						</div>
					</div>
					<div style="clear: both;">&nbsp;</div>
				</div>
				<!-- end #content -->
				<div id="sidebar">
					<ul>
						<li>
                                            <form name="search" action="/Search" method="post">
                                                     
            Search File: <input type="text" name="filename" />
   <!-- <button type="submit" style="height:50px ;width:50px;"><img src="images/search.jpg" alt="Submit"></button> -->
   <input type="submit" id="search-submit" value="GO" src/>

        </form>

						</li>
<br>
        <li>
							<h2>Upload</h2>
	<form name="upload" action="/Upload" method="post" enctype="multipart/form-data" >
            Upload Files to:
            <input type="radio" name="client" value="Dropbox" >Dropbox
            <input type="radio" name="client" value="LocalDrive" checked="checked">Local Drive<br><br>
            <input type="file" name="filename" multiple="ON">
            <input type="submit" value="Upload">
          
        </form>
    

        </li>
    <br>
						<li>
							<h2>Download</h2>
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
           
          
        </form>

						</li>
        <br>
						<li>
							<h2>Delete</h2>
								   <form name="delete" action="/Delete" method="post" >
            
        
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


						</li>
					</ul>
				</div>
				<!-- end #sidebar -->
				<div style="clear: both;">&nbsp;</div>
			</div>
		</div>
	</div>
	<!-- end #page -->
</div>
<div id="footer">
	<p>Department Of Computer Engineering ,Sinhgad College Of Engineering,Pune.</p>
</div>
<!-- end #footer -->
</body>
</html>
