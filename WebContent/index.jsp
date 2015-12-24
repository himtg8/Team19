<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>XML Upload - Team 19</title>
    </head>
 
    <body> 
        <div>
            <h3> Choose XML File to Upload</h3>
            <form action="FileUploadServlet" method="post" enctype="multipart/form-data">
                <input type="file" name="file" />
                <input type="submit" value="upload" />
            </form>          
        </div>
      
    </body>
</html>