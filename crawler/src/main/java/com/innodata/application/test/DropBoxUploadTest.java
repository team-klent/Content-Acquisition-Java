package com.innodata.application.test;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DropBoxUploadTest {

    // Securely retrieve the Dropbox Access Token from environment variables
    private static final String ACCESS_TOKEN = System.getenv("DROPBOX_ACCESS_TOKEN");

    public static void main(String[] args) {
    	//System.out.println("Dropbox Token: " + System.getenv("DROPBOX_ACCESS_TOKEN"));
        try {
            // Step 1: Let the user manually select a file
            File userFile = selectFile();

            if (userFile == null) {
                System.out.println("No file selected. Exiting...");
                return;
            }

            // Step 2: Upload the file to Dropbox
            String fileUrl = uploadFileToDropbox(userFile);

            // Step 3: Confirm successful upload
            if (fileUrl != null) {
                System.out.println("✅ File uploaded successfully! Download Link: " + fileUrl);
            } else {
                System.out.println("❌ File upload failed!");
            }

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
    }

    /**
     * Opens a file chooser dialog for the user to manually select a file.
     * @return The selected file, or null if no file was selected.
     */
    private static File selectFile() {
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

    /**
     * Uploads a file to Dropbox using the API.
     * @param file The file to be uploaded.
     * @return The Dropbox file path or null if failed.
     * @throws DbxException 
     * @throws UploadErrorException 
     */
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
