package com.example.csc325_firebase_webview_auth.view;

import com.google.api.core.ApiFuture;
import com.google.api.services.storage.Storage;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.*;
import com.google.firebase.auth.multitenancy.Tenant;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import com.google.firebase.auth.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class RegisterViewController {

    @FXML
    private VBox main;

    @FXML
    private Button createAccountButton;

    @FXML
    private TextField passwordField;

    @FXML
    private Button returnToLoginButton;

    @FXML
    private Button uploadButton;

    File currImg;

    @FXML
    private TextField emailfield;


    @FXML
    void initialize() {

    }




    @FXML
    void CreateAccount(ActionEvent event) {
        if(emailfield.getText().isEmpty() || passwordField.getText().isEmpty() || emailfield.getText().length() < 6 || passwordField.getText().length() < 6 || currImg == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter your username and password\nUsername and password need to be 6 characters minimum\nNeed to upload a photo");
            alert.showAndWait();
        }else {
            try {
                UserRecord user;
                try {
                    user = FirebaseAuth.getInstance().createUser(new UserRecord.CreateRequest().setEmail(emailfield.getText()).setPassword(passwordField.getText()));
                }catch (IllegalArgumentException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("make sure email is valid");
                    alert.showAndWait();
                    return;
                }
                DocumentReference docRef = App.fstore.collection("Users").document(UUID.randomUUID().toString());
                String imgURL;
                try {
                    imgURL = uploadImage(currImg);
                }catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("error with photo upload");
                    return;
                }
                Map<String, Object> data = new HashMap<>();
                data.put("Id", user.getUid());
                data.put("Email", emailfield.getText());
                data.put("Password", passwordField.getText());
                data.put("ImageURL",  imgURL);

                //asynchronously write data
                ApiFuture<WriteResult> result = docRef.set(data);
                main.getChildren().add(2,new Label("Loading..."));
                result.addListener(() -> {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText("You have successfully registered!");
                        alert.showAndWait();
                        try {
                            App.setRoot("/files/LoginView.fxml");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                }, Executors.newSingleThreadExecutor());

            } catch(FirebaseAuthException e){
                e.printStackTrace();
                System.out.println(e.getMessage());
            }catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("error");
                alert.showAndWait();

            }
        }



    }

    private String uploadImage(File currImg) throws IOException {
        String bucketName = "gonzalezcsc325mvvm.appspot.com"; // Replace with your bucket name
            String blobName = "images/" + currImg.getName(); // Path in the storage
            String mimeType = Files.probeContentType(currImg.toPath());

            // Automatically detect MIME type
        Blob blob = App.fstorage.bucket(bucketName).create(blobName,new FileInputStream(currImg),mimeType);
        blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        System.out.println("file uploaded");
            // Return the download URL
            return String.format("https://storage.googleapis.com/%s/%s", bucketName, blobName);

    }

    @FXML
    void returnToLogin(ActionEvent event) throws IOException {
        App.setRoot("/files/LoginView.fxml");
    }

    @FXML
    void saveImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        // Show open dialog
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if(file != null) {
            System.out.println("file not null");

            currImg = file;
            main.getChildren().add(5,new Label("Uploaded Successfully"));

        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an image file");
            alert.showAndWait();
        }
    }

}
