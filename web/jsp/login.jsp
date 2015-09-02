<%-- 
    Document   : login
    Created on : 02.09.2015, 15:40:35
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="semantic/dist/semantic.min.css">
        <link rel="stylesheet" type="text/css" href="css/standardDesign.css">
        <link rel="stylesheet" type="text/css" href="css/login.css">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="Stylesheet"type="text/css"/>
        <title>Login</title>
    </head>
    <body>
        <div class="ui segment" id="div_oben">
            <div id="div_image">
                <img class="ui small image" src="res/logo_oben.png">
                </br>
            </div>
        </div>
        <h1>Login</h1>
        <div class="ui grid" id="div_mitte">
            <div class="sixteen wide column">
                <div class="ui input" style="width: 100%">
                    <input placeholder="Benutzername" type="text">
                </div>
            </div>
            <div class="sixteen wide column">
                <div class="ui input" style="width: 100%">
                    <input placeholder="Kennwort" type="password">
                </div>
            </div>
            <div class="sixteen wide column">
                <button class="ui button" style="background-color: #007336; width: 100%; color: white;">Bestätigen</button>
            </div>
        </div>
    </body>
</html>
