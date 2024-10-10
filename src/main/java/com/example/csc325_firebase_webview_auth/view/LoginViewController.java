package com.example.csc325_firebase_webview_auth.view;

import com.example.csc325_firebase_webview_auth.model.FirestoreContext;
import com.example.csc325_firebase_webview_auth.model.Person;
import com.example.csc325_firebase_webview_auth.model.SharedData;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginViewController {

    @FXML
    private Button createButton;

    @FXML
    private Button loginButton;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField emailField;

    private SharedData sharedData;


    public void initialize() {
        sharedData = SharedData.getInstance();
    }

    @FXML
    void goToRegister(ActionEvent event) {
        try {
            App.scene.getWindow().setHeight(550);
            App.setRoot("/files/RegisterView.fxml");
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Something went wrong");
            alert.showAndWait();
        }
    }

    @FXML
    void login(ActionEvent event) {
        if(emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Email or Password is empty");
            alert.showAndWait();
            return;
        }
        try {
            UserRecord user = App.fauth.getUserByEmail(emailField.getText());
            try {
                ApiFuture<QuerySnapshot> future = App.fstore.collection("Users").get();
                // future.get() blocks on response
                List<QueryDocumentSnapshot> documents;
                documents = future.get().getDocuments();
                if (documents.size() > 0) {
                    System.out.println("Outing....");
                    for (QueryDocumentSnapshot document : documents) {
                        if (document.getString("Email").equals(user.getEmail())) {
                            if (document.getString("Password").equals(passwordField.getText())) {
                                try {

                                    sharedData.setImageUrl(document.getString("ImageURL"));
                                    sharedData.setUsername(user.getEmail());
                                    App.scene.getWindow().setHeight(600);
                                    App.scene.getWindow().setWidth(900);
                                    App.setRoot("/files/AccessFBView.fxml");
                                    return;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Email or Password Incorrect");
                    alert.showAndWait();
                    return;
                } else {
                    System.out.println("No data");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("something is wrong");
                    alert.showAndWait();

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Recheck Email");
            alert.showAndWait();
        }


    }}
