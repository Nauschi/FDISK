<%-- 
    Document   : error
    Created on : 16.02.2016, 18:10:03
    Author     : Marcel Schmidt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="semantic/dist/semantic.min.css">
        <link rel="stylesheet" type="text/css" href="css/standardDesign.css">
        <title>Error</title>
    </head>
    <body>
        <div class="ui segment" id="div_oben">
            <div id="div_image">
                <img class="ui small image" src="res/logo_oben.png">
                <br/>
            </div>
            <div class="ui menu" id="div_menu"></div>
        </div>
        <br>
        <h1 style="text-align: left; margin-left: 5%;">Es ist ein unerwarteter Fehler aufgetreten</h1>
        <form action="MainServlet" method="get">
            <button type="submit" name="button_login" style="margin-left: 5%;" class="ui button styleGruen">Zurück zum Login</button>
        </form>
    </body>
</html>
