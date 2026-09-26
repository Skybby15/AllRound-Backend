package io.github.skybby15.allround.Util;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Cors;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped 
public class FirebaseStorageUtils {
    @ConfigProperty (name = "firebase.storage.bucket")
    String bucketName;

    private final Storage storage;

  public FirebaseStorageUtils() {
    this.storage = StorageOptions.getDefaultInstance().getService();
  }

  @PostConstruct 
  void configure() {
    Bucket bucket = storage.get(bucketName);

    Cors cors = Cors.newBuilder()
            .setOrigins(List.of(Cors.Origin.of("http://localhost:3000")))
            .setMethods(List.of(
              HttpMethod.GET,
              HttpMethod.PUT,
              HttpMethod.POST,
              HttpMethod.HEAD
            ))
            .setResponseHeaders(List.of(
                    "Content-Type",
                    "Content-Length"
            ))
            .setMaxAgeSeconds(3600)
            .build();

    bucket.toBuilder()
            .setCors(List.of(cors))
            .build()
            .update();
  }

  public URL generateDownloadUrl(String storagePath) {

    BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, storagePath).build();

    return storage.signUrl(blobInfo, 15, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature());
  }

  public URL generateUploadUrl(String storagePath, String contentType) {

        BlobInfo blobInfo = BlobInfo.newBuilder(
            bucketName,
            storagePath
        )
        .setContentType(contentType)
        .build();

        return storage.signUrl(
            blobInfo,
            15,
            TimeUnit.MINUTES,
            Storage.SignUrlOption.withV4Signature(),
            Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
            Storage.SignUrlOption.withContentType()
        );
    }
}
