package io.github.skybby15.allround.Service;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped 
public class FirebaseStorageService {
    @ConfigProperty(name = "firebase.storage.bucket")
    String bucketName;

    private final Storage storage;

    public FirebaseStorageService() {
        this.storage = StorageOptions
                .getDefaultInstance()
                .getService();
    }

    public URL generateDownloadUrl(String storagePath) {

        BlobInfo blobInfo = BlobInfo.newBuilder(
                bucketName,
                storagePath
        ).build();

        return storage.signUrl(
                blobInfo,
                15,
                TimeUnit.MINUTES,
                Storage.SignUrlOption.withV4Signature()
        );
    }
}
