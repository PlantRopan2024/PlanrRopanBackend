package com.plants.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
        	 System.out.println(" run meyhod  ----");
            // Option 1: Load serviceAccountKey.json from the resources folder
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("plantroopan-firebase-adminsdk-d6hiw-c9d0e23f6b.json");
            
            
            if (serviceAccount == null) {
                throw new IllegalStateException("Could not find the serviceAccountKey.json file in the resources folder.");
            }

            
            
            // Option 2: Load serviceAccountKey.json from a specific file path (absolute path)
            // FileInputStream serviceAccount = new FileInputStream("path/to/serviceAccountKey.json");
            System.out.println("  file read  ----" + serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            
            // Initialize Firebase App (with the default name 'DEFAULT')
            FirebaseApp.initializeApp(options);

            System.out.println("FirebaseApp initialized successfully.");

        } catch (IOException e) {
            System.err.println("Failed to initialize FirebaseApp: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
  
}
