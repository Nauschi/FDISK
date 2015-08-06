<%-- 
    Document   : vordefiniert
    Created on : 02.08.2015, 13:14:24
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="semantic/dist/semantic.min.css">
        <link rel="stylesheet" type="text/css" href="css/standardDesign.css">
        <link rel="stylesheet" type="text/css" href="css/vordefiniert.css">
        <title>Vordefiniert</title>
    </head>
    <body>
        <div class="ui segment" id="div_oben">
            <div style="float: right">
                <img class="ui small image" src="res/logo_oben.png">
                </br>
            </div>

            <div class="ui pointing menu" style="background-color: #C00518; width: 100%">
                <a class="item active">
                    Vordefiniert
                </a>
                <a class="item">
                    Dynamisch
                </a>
            </div>
        </div>
        <h1>Vordefiniert</h1>
        <div class="ui segment" id="div_mitte" >
            
            <div class="ui link list" id="div_liste">
                <%
                    
                    for (int i = 0; i < 15; i++)
                    {
                        out.println("<a class='item' id='a1'> Liste" + i + "</a>");
                    }

                %>
            </div>
            <div id="div_list_vorschau">
                
            </div>
        </div>
        <script src="../semantic/dist/semantic.min.js"></script>
    </body>
</html>
