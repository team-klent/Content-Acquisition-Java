package com.innodata.application.test;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import java.util.Scanner;

public class FileUploadTest {

    private static final String UPLOAD_API_URL = "http://localhost:8080/api/files/upload"; // Spring Boot API

    public static void main(String[] args) {
        try {
            // Step 1: Let the user select a file
            File userFile = selectFile();

            if (userFile == null) {
                System.out.println("No file selected. Exiting...");
                return;
            }

            // Step 2: Upload the file via the API
            boolean uploadSuccess = uploadFile(userFile);

            // Step 3: Print result
            if (uploadSuccess) {
                System.out.println("File uploaded successfully!");
            } else {
                System.out.println("File upload failed!");
            }

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
    }

    /**
     * Open file chooser dialog for the user to select a file.
     * @return The selected file, or wala if no file .
     */
    private static File selectFile() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a file to upload");

            int result = fileChooser.showOpenDialog(null);
            return (result == JFileChooser.APPROVE_OPTION) ? fileChooser.getSelectedFile() : null;

        } catch (Exception e) {
            System.err.println("Error selecting file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Upload file using an HTTP POST request to the Spring Boot API.
     * @param file The file to be uploaded.
     * @return true - successful, else false .
     */
    private static boolean uploadFile(File file) {
        try {
            // Open connection to API
            HttpURLConnection connection = (HttpURLConnection) new URL(UPLOAD_API_URL).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data");

            // Write file data to request
            Files.copy(file.toPath(), connection.getOutputStream());

            // Get response code
            int responseCode = connection.getResponseCode();
            return responseCode == 200;

        } catch (IOException e) {
            System.err.println("Upload failed: " + e.getMessage());
            return false;
        }
    }
}
