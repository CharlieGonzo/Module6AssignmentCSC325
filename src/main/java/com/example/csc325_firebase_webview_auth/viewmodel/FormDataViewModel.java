package com.example.csc325_firebase_webview_auth.viewmodel;


import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FormDataViewModel {

    private final StringProperty userName = new SimpleStringProperty();
    private final StringProperty userMajor = new SimpleStringProperty();
    private final ReadOnlyBooleanWrapper writePossible = new ReadOnlyBooleanWrapper();

    public FormDataViewModel() {
        writePossible.bind(userName.isNotEmpty().and(userMajor.isNotEmpty()));
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public StringProperty userMajorProperty() {
        return userMajor;
    }

    public ReadOnlyBooleanProperty isWritePossibleProperty() {
        return writePossible.getReadOnlyProperty();
    }




}
