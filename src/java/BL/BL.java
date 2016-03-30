
package BL;

/**
 *
 * @author Yvonne
 */
public class BL
{
 
    /**
     * Formartiert jeweils den ersten Buchstaben jedes Wortes (abgesehen von ein paar Ausnahmen)
     * groß und die restlichen Buchstaben klein. 
     * @param strFormat
     * @return 
     */
     public String formatiereAusgabe(String strFormat) {
        if (strFormat == null || strFormat.equals("") || strFormat.isEmpty() || strFormat.equals(" ")) {
            return "";
        }
        String[] strFormatTeile = strFormat.split("(?<= )|(?<=\\/)|(?<=-)|(?<=,)|(?<=\")");
        StringBuilder strNeuesFormat = new StringBuilder("");

        if (strFormatTeile.length > 1) {
            for (String word : strFormatTeile) {

                if (word.length() > 1) {

                    if (word.equals("bis") || word.equals("zum") || word.equals("von")
                            || word.equals("bei") || word.equals("und")
                            || word.equals("zum") || word.equals("für")
                            || word.equals("beim") || word.equals("bei")
                            || word.equals("und") || word.equals("an")
                            || word.equals("der") || word.equals("die")) {
                        strNeuesFormat.append(word.toLowerCase());
                    } else if (word.equals("FWZS")) {
                        strNeuesFormat.append(word.toUpperCase());
                    } else {
                        strNeuesFormat.append(word.substring(0, 1).toUpperCase());
                        strNeuesFormat.append(word.substring(1).toLowerCase());
                    }

                } else {
                    strNeuesFormat.append(word);
                }

            }
        } else if (strFormatTeile.length == 1) {
            if (strFormat.length() > 1) {
                strNeuesFormat.append(strFormat.substring(0, 1).toUpperCase());
                strNeuesFormat.append(strFormat.substring(1).toLowerCase());
            } else {
                strNeuesFormat.append(strFormat);
            }

        }

        return strNeuesFormat.toString();
    }
}
