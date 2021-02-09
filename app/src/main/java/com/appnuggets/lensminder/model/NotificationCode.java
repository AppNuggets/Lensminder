package com.appnuggets.lensminder.model;

public enum NotificationCode {
    LENSES_EXPIRED(10),
    DROPS_EXPIRED(20),
    SOLUTION_EXPIRED(30),
    CONTAINER_EXPIRED(40);

    int code;
    NotificationCode(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
