package com.innodata.application.crawler.controller;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.UIManager;

@RestController
@RequestMapping("/api/files")
public class CrawlerController {

    private static final String ACCESS_TOKEN = System.getenv("DROPBOX_ACCESS_TOKEN"); // Securely store token

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws UploadErrorException, DbxException {
        try {

        	System.out.print("Deyn");
            File userFile = mockFileAcquisition();
            
            if (userFile == null) {
                return ResponseEntity.internalServerError().body("No file selected");
            }

            // Step 2: Upload the file to Dropbox
            String fileUrl = uploadFileToDropbox(userFile);

            // Step 3: Confirm successful upload
            if (fileUrl != null) {
                System.out.println("✅ File uploaded successfully! Download Link: " + fileUrl);
            } else {
                System.out.println("❌ File upload failed!");
            }

            // Upload file to Dropbox
            DbxRequestConfig config = new DbxRequestConfig("JavaDropboxApp");
            DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);


        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("❌ File upload failed: " + e.getMessage());
        }
        
        // Return response to front end
        return ResponseEntity.ok("Success!!");
    }

    private File mockFileAcquisition() throws IOException {
    	
    	try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Native UI appearance
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a file to upload");

            int result = fileChooser.showOpenDialog(null);
            return (result == JFileChooser.APPROVE_OPTION) ? fileChooser.getSelectedFile() : null;

        } catch (Exception e) {
            System.err.println("Error selecting file: " + e.getMessage());
            return null;
        }
    }
    
    private static String uploadFileToDropbox(File file) throws UploadErrorException, DbxException {
        try {
            // Check if access token exists
            if (ACCESS_TOKEN == null || ACCESS_TOKEN.isEmpty()) {
                System.err.println("❌ Error: Dropbox Access Token is not set!");
                return null;
            }

            // Initialize Dropbox Client
            DbxRequestConfig config = new DbxRequestConfig("JavaDropboxApp");
            DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

            // Upload file to Dropbox
            try (FileInputStream fis = new FileInputStream(file)) {
                FileMetadata metadata = client.files().uploadBuilder("/" + file.getName())
                    .uploadAndFinish(fis);
                return "https://www.dropbox.com/home" + metadata.getPathLower();
            }

        } catch (IOException e) {
            System.err.println("File upload failed: " + e.getMessage());
            return null;
        }
    }
}
