/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$('.ui.dropdown').dropdown();





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


function onErstellen(intZahler)
{
    var strWertVonLetzemSelect = document.getElementById("select_verknuepfung_" + intZahler).value;
    if (strWertVonLetzemSelect != " ")
    {
        alert("Letzte Verknüpfung muss Leer sein");
        document.getElementById("div_verknuepfung_" + intZahler).style.backgroundColor = "#C00518";
        return;
    }
    alert("Weiter zum erstellen");
}

