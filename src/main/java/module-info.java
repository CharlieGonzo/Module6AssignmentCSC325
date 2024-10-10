module com.example.csc325_firebase_webview_auth {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires jdk.jsobject;
    requires javafx.web;
    requires com.google.auth.oauth2;
    requires google.cloud.firestore;
    requires firebase.admin;
    requires com.google.api.apicommon;

    requires google.cloud.core;
    requires com.google.auth;
    requires java.desktop;
    requires com.google.api.services.storage;
    requires google.cloud.storage;
    requires io.netty.common;
    requires gapic.google.cloud.storage.v2;
    requires java.sql;


    opens com.example.csc325_firebase_webview_auth.viewmodel to jdk.jsobject;
    exports com.example.csc325_firebase_webview_auth.viewmodel;
    opens com.example.csc325_firebase_webview_auth.view;
    exports com.example.csc325_firebase_webview_auth.view;
    exports com.example.csc325_firebase_webview_auth.model;
    opens com.example.csc325_firebase_webview_auth.model;
}