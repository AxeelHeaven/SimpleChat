package com.axeelheaven.simplechat.enums;

import java.util.ArrayList;

import org.bukkit.ChatColor;

public enum ColorEnum {
    BLUE("9", ChatColor.BLUE),
    BLACK("0", ChatColor.BLACK),
    DARK_BLUE("1", ChatColor.DARK_BLUE),
    DARK_GREEN("2", ChatColor.DARK_GREEN),
    DARK_AQUA("3", ChatColor.DARK_AQUA),
    DARK_RED("4", ChatColor.DARK_RED),
    DARK_PURPLE("5", ChatColor.DARK_PURPLE),
    GOLD("6", ChatColor.GOLD),
    GRAY("7", ChatColor.GRAY),
    DARK_GRAY("8", ChatColor.DARK_GRAY),
    GREEN("a", ChatColor.GREEN),
    AQUA("b", ChatColor.AQUA),
    RED("c", ChatColor.RED),
    LIGHT_PURPLE("d", ChatColor.LIGHT_PURPLE),
    YELLOW("e", ChatColor.YELLOW),
    WHITE("f", ChatColor.WHITE);

    private final String colorId;

    private final ChatColor color;

    ColorEnum(final String colorId, final ChatColor color) {
        this.colorId = colorId;
        this.color = color;
    }

    public String getColorId() {
        return "&" + this.colorId;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public static ArrayList<ColorEnum> getColors() {
        ArrayList<ColorEnum> test = new ArrayList<>();
        for (ColorEnum color2 : values())
            test.add(color2);
        return test;
    }

    public static String convertColorCode(String s) {
        for (ColorEnum color : getColors()) {
            if (color.toString().equalsIgnoreCase(s))
                return color.getColorId();
        }
        return null;
    }

    public static String convertColor(String s) {
        for (ColorEnum colorEnum : getColors()){
            if (colorEnum.toString().equals(s))
                return colorEnum.getColor().toString();
        }
        return null;
    }
}

