package com.topjet.common;

import android.app.Application;

public class App {
    private static Application sApp;

    public static Application getInstance() {
        return sApp;
    }


    public static void init(Application app) {
        sApp = app;
    }
}
