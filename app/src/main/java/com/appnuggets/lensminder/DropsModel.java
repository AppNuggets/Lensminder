package com.appnuggets.lensminder;

public class DropsModel {

    String name;
    String from;
    String to;

    public DropsModel(String name, String from, String to){
        this.name = name;
        this.from = from;
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

}
