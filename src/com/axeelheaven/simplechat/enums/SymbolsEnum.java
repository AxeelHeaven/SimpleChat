package com.axeelheaven.simplechat.enums;

import java.util.ArrayList;

public enum SymbolsEnum {
    DEFAULT(""),
    STAR("✪"),
    FLOWER("✿"),
    CLOUD("☁"),
    SUN("☀"),
    HORSE("♞"),
    AVION("✈"),
    COOL("✔"),
    POINT("•"),
    HEART("❤"),
    JAVA("♨"),
    UMBRELLA("☂"),
    PENCIL("✎"),
    SCISSORS("✂"),
    CLOVER("♣");

    private final String symbol;

    SymbolsEnum(String symbol) {
        this.symbol = symbol;
    }
    public String getSymbol() {
        return this.symbol;
    }

    public static ArrayList<SymbolsEnum> getSymbols() {
        ArrayList<SymbolsEnum> test = new ArrayList<>();
        for (SymbolsEnum symbols2 : values())
            test.add(symbols2);
        return test;
    }

    public static String convertSymbols(String s) {
        for (SymbolsEnum symbolsEnum : getSymbols()) {
            if (symbolsEnum.toString().equalsIgnoreCase(s))
                return symbolsEnum.getSymbol();
        }
        return null;
    }
}
