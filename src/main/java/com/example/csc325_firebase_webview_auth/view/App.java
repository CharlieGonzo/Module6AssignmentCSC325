package com.example.csc325_firebase_webview_auth.view;


import com.example.csc325_firebase_webview_auth.model.FirestoreContext;
import com.example.csc325_firebase_webview_auth.model.SharedData;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import com.google.firebase.cloud.StorageClient;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Firestore fstore;
    public static FirebaseAuth fauth;
    public static StorageClient fstorage;
    public static Scene scene;

    private final FirestoreContext contxtFirebase = new FirestoreContext();

    @Override
    public void start(Stage primaryStage) throws Exception {

        fstore = contxtFirebase.firebase();
        fauth = FirebaseAuth.getInstance();
        fstorage = StorageClient.getInstance();

        SharedData.getInstance();

        Image splashImage = new Image(getClass().getResourceAsStream("/files/rams.jpeg"));
        ImageView splashImageView = new ImageView(splashImage);


        StackPane root = new StackPane(splashImageView);
        scene = new Scene(root, 600, 400); // Set the desired width and height


        primaryStage.setScene(scene);
        primaryStage.show();


        // Add a fade-in and fade-out transition for splash screen
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), splashImageView);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.5), splashImageView);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(2)); // Show the splash for 2 seconds before fading out

        // Chain fade-in and fade-out and load the main window after
        fadeIn.setOnFinished(e -> fadeOut.play());
        fadeOut.setOnFinished(e -> {
            primaryStage.close();
            showMainApp(primaryStage);
        });

        fadeIn.play();
    }

    public static void setRoot(String fxml) throws IOException {

        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml ));
        return fxmlLoader.load();
    }

    private void showMainApp(Stage primaryStage) {
        // Set up the main application window
        try {
            scene = new Scene(loadFXML("/files/LoginView.fxml"));
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

}
