/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//Initialisiert die Dropdowns
$('.ui.dropdown').dropdown();



//Wird aufgerufen wenn in einem Dropdown wo der Typ bestimmt wird sich etwas ändert
/*
 * Stellt je nach Typ in der Zeile, den Filter auf ein Dropdown, Textfield oder Datepicker
 */
function onTypChanged(select_typ)
{
    var strTyp = select_typ.value;
    var strID = select_typ.getAttribute('id');
    strID = strID.split("_")[2];
    var strBoxArt = strTyp.split(";")[1];
    if (strBoxArt == "cb")
    {
        document.getElementById("div_filter_cb_" + strID).style.display = "block";
        document.getElementById("div_filter_txt_" + strID).style.display = "none";
        document.getElementById("div_filter_datepicker_" + strID).style.display = "none";
    } else if (strBoxArt == "txt")
    {
        document.getElementById("div_filter_cb_" + strID).style.display = "none";
        document.getElementById("div_filter_txt_" + strID).style.display = "block";
        document.getElementById("div_filter_datepicker_" + strID).style.display = "none";
    } else if (strBoxArt == "datepicker")
    {
        document.getElementById("div_filter_cb_" + strID).style.display = "none";
        document.getElementById("div_filter_txt_" + strID).style.display = "none";
        document.getElementById("div_filter_datepicker_" + strID).style.display = "block";
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

