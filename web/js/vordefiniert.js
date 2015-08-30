/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function onListItemClicked(item)
{
    var items = document.getElementById("div_liste").getElementsByTagName("a");
    var index;
    for (index = 0; index < items.length; ++index) {
        items[index].className = "item";
    }
    item.className = item.className + " active";
    
    var text = item.getElementsByTagName("div")[0].innerHTML;
    
    document.getElementById("div_daten").getElementsByTagName("h2")[0].innerHTML = item.getElementsByTagName("span")[0].innerHTML;
    document.getElementById("div_table").innerHTML=text;
    
    
}