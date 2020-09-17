package com.codecool.poster_xd_api;

import com.codecool.poster_xd_api.helpers.DBSetter;


public class App {
    public static void main(String[] args) {
        new DBSetter().init();
    }
}
