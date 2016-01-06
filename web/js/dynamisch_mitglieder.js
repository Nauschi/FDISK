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
    map = otherMap;
}


//Wird aufgerufen wenn in einem Dropdown wo der Typ bestimmt wird sich etwas ändert
/*
 * Stellt je nach Typ in der Zeile, den Filter auf ein Dropdown, Textfield oder Datepicker
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
        aktualisiereOperator(strID,operatorFeld);
//        document.getElementById("select_operator_"+strID).selectedIndex = "0"; geht irgendwie nd.. vl Framework das problem... (http://www.w3schools.com/jsref/tryit.asp?filename=tryjsref_select_selectedindex)
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
        aktualisiereOperator(strID,operatorFeld);
    }

}

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

    if (map['Anrede'] != undefined)
    {
        $('#select_filter_cb_' + strID).dropdown('clear');

        var div_filter = document.getElementById("div_filter_cb_" + strID);

        div_filter.innerHTML = '<select name="select_filter_cb_' + strID + '>" class="ui fluid dropdown" id="select_filter_cb_' + strID + '"></select>';
        var select_filter = document.getElementById("select_filter_cb_" + strID);

        var strHTMLFilter = map['Anrede'];
        var strSplitFilter = strHTMLFilter.split(';');
        for (var i = 0; i < strSplitFilter.length; i++)
        {
            var opt = document.createElement('option');
            opt.value = strSplitFilter[i];
            opt.innerHTML = strSplitFilter[i];
            select_filter.appendChild(opt);
        }
        $('#select_filter_cb_' + strID).dropdown();
    }
}


function onChangeVerknuepfung(intIndexVonZeile)
{

    document.getElementById("div_verknuepfung_" + intIndexVonZeile).style.backgroundColor = "white";
}

//Wir aufgerufen wenn der Button erstellen gecklickt wird.
/*
 * Überprüft ob die letzte Verknüpfung leer ist, 
 * falls sie leer ist wird das div rot eingefärbt,
 * falls nicht....
 */
function onErstellen(intZahler)
{
    var strWertVonLetztemSelect = document.getElementById("select_verknuepfung_" + intZahler).value;
    if (strWertVonLetztemSelect != " ")
    {
        alert("Letzte Verknüpfung muss leer sein!");
        document.getElementById("div_verknuepfung_" + intZahler).style.backgroundColor = "#C00518";
        return;
    }
    alert("Weiter zum Erstellen");
}

