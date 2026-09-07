package io.github.skybby15.allround.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class FirebaseConfig {
    @ConfigProperty(name = "firebase.storage.bucket")
    String bucketName;

    @PostConstruct
    public void initialize() throws IOException {

        if (!FirebaseApp.getApps().isEmpty()) {
            return;
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(
                        //requires the GOOGLE_APPLICATION_CREDENTIALS environment variable to be set to the path of the service account key file
                        GoogleCredentials.getApplicationDefault()
                )
                .setStorageBucket(
                        bucketName
                )
                .build();

        FirebaseApp.initializeApp(options);
    }
}
