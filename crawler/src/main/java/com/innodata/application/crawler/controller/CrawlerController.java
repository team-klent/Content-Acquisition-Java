package com.innodata.application.controller;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.FileEntity;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class CrawlerController {

    private static final String FILE_IO_URL = "https://limewire.com/";

    //1st API endpoint start
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Convert MultipartFile to a temporary File
            File tempFile = convertMultipartFile(file);

            // HTTP POST request to File.io/limewire -- kay free man ni
            HttpPost uploadRequest = new HttpPost(FILE_IO_URL);
            //NULL match type
            InputStreamEntity entity = new InputStreamEntity(new FileInputStream(tempFile), null);
            uploadRequest.setEntity(entity);

            // Execute request and get response
            var response = httpClient.execute(uploadRequest);
            String jsonResponse = new String(response.getEntity().getContent().readAllBytes());

            // Clean up temporary file
            tempFile.delete();

            // Extract file URL from File.io/limewire response
            JSONObject json = new JSONObject(jsonResponse);
            String fileUrl = json.has("link") ? json.getString("link") : "No file URL returned";

           //temp lang niii 
            System.out.println("File uploaded successfully! Download Link: " + fileUrl);
            return ResponseEntity.ok("File uploaded successfully! URL: " + fileUrl);
        } catch (IOException e) {
        	//FAIL　かくにんします
            System.err.println("File upload failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body("File upload failed: " + e.getMessage());
        }
    }

    private File convertMultipartFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }
}
