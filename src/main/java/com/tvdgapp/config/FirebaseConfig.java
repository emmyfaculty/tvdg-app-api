package com.tvdgapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.tvdgapp.exceptions.ServiceException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // Check if a FirebaseApp with the default name already exists
        if (FirebaseApp.getApps().isEmpty()) {
            // Load the service account key JSON file
            var serviceAccount = this.loadServiceAccountKey("reference/serviceAccountKey.json");

            // Create and initialize Firebase options
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setDatabaseUrl("https://your-database-name.firebaseio.com")
                    .build();

            // Initialize the default app
            return FirebaseApp.initializeApp(options);
        } else {
            // If already exists, return the existing instance
            return FirebaseApp.getInstance();
        }
    }

    private InputStream loadServiceAccountKey(String jsonFilePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(jsonFilePath);
            if (in == null) {
                throw new IOException("Service account key file not found: " + jsonFilePath);
            }
            return in;
        } catch (Exception e) {
            throw new ServiceException("Failed to load service account key file", e);
        }
    }

}
