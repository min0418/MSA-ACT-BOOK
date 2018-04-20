package com.example.demo;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FireBaseManager {

    public FireBaseManager(){
        initFireBase();
    }

    public void initFireBase(){

        FileInputStream serviceAccount = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            serviceAccount = new FileInputStream(classLoader.getResource("act-book-firebase-adminsdk-4f0ru-b409b7f0f0.json").getFile());
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        new FireBaseManager();
    }„
}
