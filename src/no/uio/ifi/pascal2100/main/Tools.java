package no.uio.ifi.pascal2100.main;


import java.util.ArrayList;

public class Tools {
    /**
     * implode(delimiter, array of strings)
     * Puts delimiter between the strings and returns the resulting string.
     * <p/>
     * PS: Mostly from http://stackoverflow.com/a/11248692
     * But this is a pretty generic piece of code, so it should be okay.
     */
    public static String implode(String delim, ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size() - 1; i++) {
            sb.append(list.get(i));
            sb.append(delim);
        }
        sb.append(list.get(list.size() - 1));
        return sb.toString();
    }
}
