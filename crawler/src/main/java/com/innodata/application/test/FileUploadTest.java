package com.innodata.application.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import org.json.JSONObject;

public class FileUploadTest {

    private static final String FILE_IO_URL = "https://file.io";

    public static void main(String[] args) {
        try {
            // Step 1: Let the user select a file
            File userFile = selectFile();

            if (userFile == null) {
                System.out.println("❌ No file selected. Exiting...");
                return;
            }

            // Step 2: Upload the file to File.io
            String response = uploadFile(userFile);

            // Step 3: Verify upload success
            if (response != null) {
                System.out.println("✅ File uploaded successfully!");
                JSONObject jsonResponse = new JSONObject(response);
                if (jsonResponse.has("link")) {
                    System.out.println("🔗 Download Link: " + jsonResponse.getString("link"));
                }
            } else {
                System.out.println("❌ File upload failed!");
            }

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
    }

    /**
     * Opens a file chooser dialog for the user to select a file.
     * @return The selected file, or null if none.
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
     * Uploads the file using an HTTP POST request to File.io.
     * @param file The file to be uploaded.
     * @return JSON response confirming success or null if failed.
     */
    private static String uploadFile(File file) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(FILE_IO_URL).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data");

            // Write file data to request
            try (OutputStream os = connection.getOutputStream(); FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }

            // Get response code
            int responseCode = connection.getResponseCode();

            // Read and return response if successful
            if (responseCode == 200) {
                Scanner scanner = new Scanner(connection.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();
                return response.toString();
            } else {
                return null;
            }

        } catch (Exception e) {
            System.err.println("Upload failed: " + e.getMessage());
            return null;
        }
    }
}
