<%-- 
    Document   : SuccessNewAccount
    Created on : Jan 18, 2015, 5:09:46 PM
    Author     : Rohit
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
<meta name="viewport" content="width=device-width, initial-scale=1.0"> 
  <title> Success New Account </title>
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
	<h1><strong>New Account Created Successfully  !!!</strong> </h1>
        <br><br>
        <h2>
            Your Account ID is :<%=request.getAttribute("aid")%>
            <br><br>
            Please Do Not Share your Account ID other than your Group<br><br> 	
        </h2>
<div class="support-note">
	<span class="note-ie">Sorry, only modern browsers.</span>
</div>
				
</header>
			

	<form class="container">
		<p>
                <center><h1><strong> Please <a href="index.html">Login</a></strong></h1></center>

		</p>

 
	</form>
			
</div>

    </body>
</html>
