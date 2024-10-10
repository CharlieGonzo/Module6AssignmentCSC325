package com.example.csc325_firebase_webview_auth.view;//package modelview;

import com.example.csc325_firebase_webview_auth.model.Person;
import com.example.csc325_firebase_webview_auth.model.SharedData;
import com.example.csc325_firebase_webview_auth.viewmodel.AccessDataViewModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class AccessFBView {
    @FXML
    private TableColumn<Person, String> age;

    @FXML
    private TableColumn<Person, String> major;


    @FXML
    private TableColumn<Person, String> name;


    @FXML
    private TextField nameField;
    @FXML
    private TextField majorField;
    @FXML
    private TextField ageField;
    @FXML
    private Button writeButton;
    @FXML
    private Button readButton;
    @FXML
    private TextArea outputField;
    @FXML
    private ImageView profileImg;
    @FXML
    private TableView<Person> persons;

    private SharedData sharedData;
     private boolean key;
    private ObservableList<Person> listOfUsers = FXCollections.observableArrayList();
    private Person person;
    public ObservableList<Person> getListOfUsers() {
        return listOfUsers;
    }

    @FXML
    void initialize() throws URISyntaxException, MalformedURLException {
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        major.setCellValueFactory(new PropertyValueFactory<>("major"));
        sharedData = SharedData.getInstance();
        System.out.println(sharedData.getImageUrl());
        String path = downloadImage(sharedData.getImageUrl());
        System.out.println(path);
    Image image = new Image(path);
    profileImg.setImage(image);



        AccessDataViewModel accessDataViewModel = new AccessDataViewModel();
        nameField.textProperty().bindBidirectional(accessDataViewModel.userNameProperty());
        majorField.textProperty().bindBidirectional(accessDataViewModel.userMajorProperty());
        writeButton.disableProperty().bind(accessDataViewModel.isWritePossibleProperty().not());
    }

    @FXML
    private void addRecord(ActionEvent event) {
        addData();
    }

        @FXML
    private void readRecord(ActionEvent event) {
        readFirebase();
    }

            @FXML
    private void regRecord(ActionEvent event) {
        registerUser();
    }

     @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("/files/WebContainer.fxml");
    }

    public void addData() {

        DocumentReference docRef = App.fstore.collection("References").document(UUID.randomUUID().toString());

        Map<String, Object> data = new HashMap<>();
        data.put("Name", nameField.getText());
        data.put("Major", majorField.getText());
        data.put("Age", Integer.parseInt(ageField.getText()));
        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
    }

        public boolean readFirebase()
         {
             key = false;


        ApiFuture<QuerySnapshot> future =  App.fstore.collection("References").get();

        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(documents.size()>0)
            {
                System.out.println("Outing....");
                for (QueryDocumentSnapshot document : documents)
                {
//                    outputField.setText(outputField.getText()+ document.getData().get("Name")+ " , Major: "+
//                            document.getData().get("Major")+ " , Age: "+
//                            document.getData().get("Age")+ " \n ");
                    System.out.println(document.getId() + " => " + document.getData().get("Name"));
                    person  = new Person(String.valueOf(document.getData().get("Name")),
                            document.getData().get("Major").toString(),
                            Integer.parseInt(document.getData().get("Age").toString()));
                    listOfUsers.add(person);
                    persons.setItems(listOfUsers);
                }
            }
            else
            {
               System.out.println("No data");
            }
            key=true;

        }
        catch (InterruptedException | ExecutionException ex)
        {
             ex.printStackTrace();
        }
        return key;
    }

        public void sendVerificationEmail() {
        try {
            UserRecord user = App.fauth.getUser("name");


        } catch (Exception e) {
        }
    }

    public boolean registerUser() {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail("user@example.com")
                .setEmailVerified(false)
                .setPassword("secretPassword")
                .setPhoneNumber("+11234567890")
                .setDisplayName("John Doe")
                .setDisabled(false);

        UserRecord userRecord;
        try {
            userRecord = App.fauth.createUser(request);
            System.out.println("Successfully created new user: " + userRecord.getUid());
            return true;

        } catch (FirebaseAuthException ex) {
           // Logger.getLogger(FirestoreContext.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    private static String downloadImage(String imageUrl) {
        String fileName;
        try {
            URL url = new URL(imageUrl);
            fileName = getFileName(url);
            File file = new File(fileName);

            try (InputStream in = new BufferedInputStream(url.openStream());
                 FileOutputStream out = new FileOutputStream(file)) {

                byte[] data = new byte[1024];
                int count;
                while ((count = in.read(data, 0, data.length)) != -1) {
                    out.write(data, 0, count);
                }
                System.out.println("Downloaded: " + file.getAbsolutePath());
                return file.toURI().toString();

            }
        } catch (IOException e) {
            System.err.println("Error downloading image from " + imageUrl + ": " + e.getMessage());
        }

        return "";
    }

    private static String getFileName(URL url) {
        String fileName = url.getPath();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }
}
