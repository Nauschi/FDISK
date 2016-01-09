/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var map = {'Test': 'anushgjk'};


//Initialisiert die Dropdowns
$('.ui.dropdown').dropdown();



function setMap(otherMap)
{
    alert("dynamisch-Mitglieder.js.setMap: setMap");
    map = otherMap;
}


//Wird aufgerufen wenn in einem Dropdown wo der Typ bestimmt wird sich etwas ändert
/**
 * Stellt je nach Typ in der Zeile, den Filter auf ein Dropdown, Textfield oder Datepicker
 * @param {type} select_typ
 * @returns {undefined}
 */
function onTypChanged(select_typ)
{
    var strTypPlusBox = select_typ.value;
    var strID = select_typ.getAttribute('id');
    strID = strID.split("_")[2];
    var strBoxArt = strTypPlusBox.split(";")[1];
    var strTyp = strTypPlusBox.split(";")[0];
    if (strBoxArt == "cb")
    {

        document.getElementById("div_filter_cb_" + strID).style.display = "block";
        document.getElementById("div_filter_txt_" + strID).style.display = "none";
        document.getElementById("div_filter_datepicker_" + strID).style.display = "none";
        initialisiereCBFilter(strTyp, strID);


    } else if (strBoxArt == "txt")
    {
        var operatorFeld = ["=", "<>"];
        aktualisiereOperator(strID, operatorFeld);
        document.getElementById("div_filter_cb_" + strID).style.display = "none";
        document.getElementById("div_filter_txt_" + strID).style.display = "block";
        document.getElementById("div_filter_datepicker_" + strID).style.display = "none";
    } else if (strBoxArt == "datepicker")
    {
        document.getElementById("div_filter_cb_" + strID).style.display = "none";
        document.getElementById("div_filter_txt_" + strID).style.display = "none";
        document.getElementById("div_filter_datepicker_" + strID).style.display = "block";
    }

    if ((strBoxArt == "datepicker" || strBoxArt == "cb") && document.getElementById("select_operator_" + strID).options[2] == null)
    {
        var operatorFeld = ["=", "<>", "<=", ">=", "<", ">"];
        aktualisiereOperator(strID, operatorFeld);
    }

}
/**
 * 
 * @param {type} strID
 * @param {type} operatorFeld
 * @returns {undefined}
 */
function aktualisiereOperator(strID, operatorFeld)
{
    var div_operator = document.getElementById("div_operator_" + strID);
    div_operator.innerHTML = '<select name="select_operator_' + strID + '" class="ui fluid dropdown" id="select_operator_' + strID + '">';
    var select_operator = document.getElementById("select_operator_" + strID);
    for (var i = 0; i < operatorFeld.length; i++)
    {
        var opt = document.createElement('option');
        opt.value = operatorFeld[i];
        opt.innerHTML = operatorFeld[i];
        select_operator.appendChild(opt);
    }
    $('#select_operator_' + strID).dropdown();
}


function initialisiereCBFilter(strTyp, strID)
{
    alert(strTyp.toUpperCase());
    if (map[strTyp.toUpperCase()] != undefined)
    {
        var div_filter = document.getElementById("div_filter_cb_" + strID);

        div_filter.innerHTML = '<select name="select_filter_cb_' + strID + '>" class="ui fluid dropdown" id="select_filter_cb_' + strID + '"></select>';
        var select_filter = document.getElementById("select_filter_cb_" + strID);

        var strHTMLFilter = map[strTyp.toUpperCase()];
        var strSplitFilter = strHTMLFilter.split(';');
        for (var i = 0; i < strSplitFilter.length; i++)
        {
            var opt = document.createElement('option');
            opt.value = strSplitFilter[i];
            opt.innerHTML = strSplitFilter[i];
            select_filter.appendChild(opt);
        }
        $('#select_filter_cb_' + strID).dropdown();
    }else
    {
        alert('not in');
    }
}


function onChangeVerknuepfung(intIndexVonZeile)
{
    var div_verknuefung = document.getElementById("div_verknuepfung_" + intIndexVonZeile);
    var style = getStyle(div_verknuefung,'backgroundColor');
    if (style == 'rgb(192, 5, 24)'||style == '#c00518')
    {
        div_verknuefung.style.backgroundColor = "white";
    }
}

function getStyle(el,styleProp)
{
    if (el.currentStyle)
        return el.currentStyle[styleProp];

    return document.defaultView.getComputedStyle(el,null)[styleProp];
}

//Wir aufgerufen wenn der Button erstellen gecklickt wird.
/**
 * Überprüft ob die letzte Verknüpfung leer ist, 
 * falls sie leer ist wird das div rot eingefärbt,
 * falls nicht....
 * @param {type} intZahler
 * @returns {undefined}
 */
function onErstellen(intZahler)
{
    var strWertVonLetztemSelect = document.getElementById("select_verknuepfung_" + intZahler).value;
    if (strWertVonLetztemSelect != "N/A")
    {
        alert("Letzte Verknüpfung muss leer sein!");
        document.getElementById("div_verknuepfung_" + intZahler).style.backgroundColor = "#C00518";
        return;
    }
    alert("Weiter zum Erstellen");
}

