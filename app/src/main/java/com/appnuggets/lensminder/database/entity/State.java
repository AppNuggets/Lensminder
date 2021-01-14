package com.appnuggets.lensminder.database.entity;

public enum State {
    IN_USE(0),
    IN_STOCK(1),
    IN_HISTORY(2);

    private Integer code;

    State(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
