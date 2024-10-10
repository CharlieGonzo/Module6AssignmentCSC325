package com.example.csc325_firebase_webview_auth.model;

public class SharedData {
    private static SharedData currentInstance = null;

    private String id;
    private String username;
    private String ImageUrl;

    private SharedData() {
        id = "";
        username = "";
        ImageUrl = "";
    }

    public static SharedData getInstance(){
        if(currentInstance == null){
            currentInstance = new SharedData();
            return currentInstance;
        }
        return currentInstance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
