package com.elements.elements.util;

/**
 * Created by Giancarlo on 20/12/2015.
 */
public class WikiAPI {
    public static String parseOverview(String raw) {
        raw = raw.substring(raw.indexOf("'"));
        raw = raw.replaceAll("&nbsp;", " ");
        raw = raw.replaceAll("(?s)<ref.*?</ref>", "");
        raw = raw.replaceAll("[\\{][\\{]([^{}]*)[|]([^{}]*)[\\}][\\}]","$2");
        raw = raw.replaceAll("\\[\\[([^\\[]*)[|]([^\\[]*)\\]\\]","$2");
        raw = raw.replaceAll("[{][{]val[|](.*)[|]u[=](.*)[}][}]","$1 $2");
        raw = raw.replaceAll("[\\[][\\[](.*?)[\\]][\\]]","$1");
        raw = raw.replaceAll("'''(.*?)'''", "<b>$1</b>");
        return raw;
    }
}
