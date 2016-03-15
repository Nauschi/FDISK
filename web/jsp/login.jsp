<%-- 
    Document   : login
    Created on : 02.09.2015, 15:40:35
    Author     : Marcel Schmidt
--%>

<%@page import="java.util.LinkedList"%>
<%@page import="Beans.Berechtigung"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="semantic/dist/semantic.min.css">
        <link rel="stylesheet" type="text/css" href="css/standardDesign.css">
        <link rel="stylesheet" type="text/css" href="css/login.css">
        <link rel="stylesheet" type="text/css" href="css/jquery-ui_1.css">
        <link rel="stylesheet" type="text/css" href="css/jquery-ui_2.css">
        <title>Login</title>
    </head>
    <body>
        <%!
            LinkedList<Berechtigung> liBerechtigungen;

        %>
        <%
            session.setAttribute("lastPage", "login");
            liBerechtigungen = (LinkedList<Berechtigung>) request.getAttribute("berechtigungen");
        %>
        <div class="ui segment" id="div_oben">
            <div id="div_image">
                <img class="ui small image" src="res/logo_oben.png">
                <br/>
            </div>
            <div class="ui menu" id="div_menu"></div>
        </div>
        <h1>Login</h1>

        <%
            if (liBerechtigungen != null)
            {
        %>

        <div class="ui modal">
            <div class="header">
                Wählen Sie bitte eine Instanz aus
            </div>
            <div class="content">
                <form action="MainServlet" method="POST" name="form_submit">
                    <select name="select_berechtigung" class="ui fluid dropdown">
                        <%=generiereBerechtigungen()%>
                    </select>
                </form>
            </div>

            <div class="actions">
                <button type="button" onClick="document.form_submit.submit();" name="button_bestaetigen" class="ui button styleGruen"  style="width: 20%;">Bestätigen</button>
                <button type="button" onClick="$('.ui.modal').modal('hide');" name="button_abbrechen" class="ui button styleRot"  style="width: 20%;">Abbrechen</button>
            </div>
        </div>
        <%
            }
        %>

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
                    <button type="submit" name="button_login" class="ui button styleGruen" style="width: 100%;">Bestätigen</button>
                </div>
            </div>
        </form>

        <script src="js/jquery-2.1.1.min.js"></script>
        <script src="semantic/dist/semantic.min.js"></script>
        <script src="js/jquery-ui.js"></script> 
        <script>
                    $(document).ready(function () {
            <%
                if (liBerechtigungen != null)

                {
            %>

                        $('.ui.modal').modal('show');
                        $('.ui.dropdown').dropdown();
            <%
                }
            %>
                    });
        </script>
    </body>
</html>

<%!    public String generiereBerechtigungen()
    {
        String strAusgabe = "";
        if (liBerechtigungen != null)
        {

            for (Berechtigung ber : liBerechtigungen)
            {
                strAusgabe += "<option value='" + ber.getStrBerechtigung() + "'>" + ber.getStrBerechtigung() + "</option>";
            }

        }
        return strAusgabe;
    }
%>
