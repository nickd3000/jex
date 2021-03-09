package com.physmo;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

import java.util.HashMap;
import java.util.Map;

public class ColorRepo {
    public static String NORMAL_TEXT = "NORMAL_TEXT";
    public static String INFO_BAR = "INFO_BAR";
    public static String SCROLL_BAR = "SCROLL_BAR";
    String MENU_BAR = "MENU_BAR";
    String FG = "FG";
    String BG = "BG";

    Map<String, Map<String, TextColor.ANSI>> styles;

    public ColorRepo() {
        styles = new HashMap<>();
        initColors();
    }

    public void setThemeElementColor(TextGraphics tg, String element) {
        tg.setForegroundColor(styles.get("DOS").get(element + FG));
        tg.setBackgroundColor(styles.get("DOS").get(element + BG));
    }

    public void initColors() {
        Map<String, TextColor.ANSI> dosStyle = new HashMap<>();
        dosStyle.put(NORMAL_TEXT + FG, TextColor.ANSI.WHITE);
        dosStyle.put(NORMAL_TEXT + BG, TextColor.ANSI.BLUE);
        dosStyle.put(INFO_BAR + FG, TextColor.ANSI.BLACK);
        dosStyle.put(INFO_BAR + BG, TextColor.ANSI.CYAN);
        dosStyle.put(SCROLL_BAR + FG, TextColor.ANSI.WHITE);
        dosStyle.put(SCROLL_BAR + BG, TextColor.ANSI.BLACK);
        styles.put("DOS", dosStyle);
    }

//    public static void setDefaultTextColor(TextGraphics tg) {
//        tg.setForegroundColor(TextColor.ANSI.WHITE);
//        tg.setBackgroundColor(TextColor.ANSI.BLACK);
//    }
//
//    public static void setNormalTextColor(TextGraphics tg) {
//        tg.setForegroundColor(TextColor.ANSI.WHITE);
//        tg.setBackgroundColor(TextColor.ANSI.GREEN_BRIGHT);
//    }
//
//    public static void setInfoBarTextColor(TextGraphics tg) {
//        tg.setForegroundColor(TextColor.ANSI.WHITE);
//        tg.setBackgroundColor(TextColor.ANSI.GREEN);
//    }
//
//    public static void setScrollBarTextColor(TextGraphics tg) {
//        tg.setForegroundColor(TextColor.ANSI.WHITE);
//        tg.setBackgroundColor(TextColor.ANSI.RED);
//    }
}
