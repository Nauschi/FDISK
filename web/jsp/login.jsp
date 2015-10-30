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
        <link rel="stylesheet" href="css/jquery-ui_1.css">
        <link href="css/jquery-ui_2.css" rel="Stylesheet"type="text/css"/>
        <title>Login</title>
    </head>
    <body>
        <%
            session.setAttribute("lastPage", "login");
        %>
        <div class="ui segment" id="div_oben">
            <div id="div_image">
                <img class="ui small image" src="res/logo_oben.png">
                <br/>
            </div>
            <div class="ui menu" style="background-color: #C00518; width: 100%"></div>
        </div>
        <h1>Login</h1>
        <form action="MainServlet" method="POST">
            <div class="ui grid" id="div_mitte">
                <div class="sixteen wide column">
                    <div class="ui input" style="width: 100%">
                        <input placeholder="Benutzername" type="text" name="input_benutzername">
                    </div>
                </div>
                <div class="sixteen wide column">
                    <div class="ui input" style="width: 100%">
                        <input placeholder="Kennwort" type="password" name="input_kennwort">
                    </div>
                </div>
                <%
                    if (request.getAttribute("login_error") != null)
                    {
                %>
                <div>
                    <p style="color: red">Benutzername oder Kennwort ist falsch!</p>
                </div>
                <%
                    }
                %>
                <div class="sixteen wide column">
                    <button type="submit" name="button_login" class="ui button styleGruen" style="background-color: #007336; width: 100%; color: white;">Bestätigen</button>
                </div>
            </div>
        </form>
    </body>
</html>
