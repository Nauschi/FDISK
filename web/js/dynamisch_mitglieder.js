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
        $("#select_operator_" + strID + " option[value='<=']").remove();
        $("#select_operator_" + strID + " option[value='>=']").remove();
        $("#select_operator_" + strID + " option[value='<']").remove();
        $("#select_operator_" + strID + " option[value='>']").remove();
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
        select_operator = document.getElementById("select_operator_" + strID);
        var opt1 = document.createElement('option');
        opt1.value = "<=";
        opt1.innerHTML = "<=";
        select_operator.appendChild(opt1);
        var opt2 = document.createElement('option');
        opt2.value = ">=";
        opt2.innerHTML = ">=";
        select_operator.appendChild(opt2);
        var opt3 = document.createElement('option');
        opt3.value = "<";
        opt3.innerHTML = "<";
        select_operator.appendChild(opt3);
        var opt4 = document.createElement('option');
        opt4.value = ">";
        opt4.innerHTML = ">";
        select_operator.appendChild(opt4);
    }

}


function removeOptions(selectbox)
{
    var i;
    for (i = selectbox.options.length - 1; i >= 0; i--)
    {
        selectbox.remove(i);
    }
}


function initialisiereCBFilter(strTyp, strID)
{
    if (map[strTyp] != undefined)
    {
        var select_filter = document.getElementById("select_filter_cb_" + strID);
        removeOptions(select_filter);
        var strHTMLFilter = map['Anrede'];
        var strSplitFilter = strHTMLFilter.split(';');
        for (var i = 0; i < strSplitFilter.length; i++) {
            var opt = document.createElement('option');
            opt.value = strSplitFilter[i];
            opt.innerHTML = strSplitFilter[i];
            select_filter.appendChild(opt);
        }
        
        //set selected index auf erstes element...
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

