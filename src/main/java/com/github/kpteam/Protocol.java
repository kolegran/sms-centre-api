package com.github.kpteam;

public enum Protocol {

    HTTP("http"),
    HTTPS("https"),
    ;

    private final String name;

    Protocol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
