package com.innodata.application.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.innodata.application.model.FileModel;
import com.innodata.application.repository.FileRepository;
import com.innodata.application.utilities.ApplicationUtils;

@Service
public class CrawlerService {

	@Value("${dropbox.access.token}")
	private String ACCESS_TOKEN;

	@Autowired
	private ApplicationUtils applicationUtils;

	@Autowired
	// JPA レポしとり
	private FileRepository fileRepository;

	public ResponseEntity<HashMap<String, Object>> uploadFileToDropbox(MultipartFile multipartFile)
			throws IOException, DbxException {
		File file = applicationUtils.fileConversion(multipartFile);

		if (file == null) {
			return applicationUtils.generateErrorResponse("❌ Error: No file selected!", HttpStatus.BAD_REQUEST);
		}

		if (ACCESS_TOKEN == null || ACCESS_TOKEN.isEmpty()) {
			return applicationUtils.generateErrorResponse("❌ Error: Dropbox Access Token is not set!",
					HttpStatus.BAD_REQUEST);
		}

		DbxRequestConfig config = new DbxRequestConfig("JavaDropboxApp");
		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

		// Generate a timestamped folder name
		String folderName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String folderPath = "/" + folderName;

		// Create the folder in Dropbox
		try {
			FolderMetadata folderMetadata = client.files().createFolderV2(folderPath).getMetadata();
			System.out.println("📁 Folder created successfully: " + folderMetadata.getPathLower());
		} catch (DbxException e) {
			// Avoid redundant folder creation errors
			if (!e.getMessage().contains("conflict")) {
				return applicationUtils.generateErrorResponse("❌ Error: Folder creation failed (may already exist)!",
						HttpStatus.CONFLICT);
			}
		}

		// Upload the file into the newly created folder
		String filePath = folderPath + "/" + file.getName();
		try (FileInputStream fis = new FileInputStream(file)) {
			FileMetadata metadata = client.files().uploadBuilder(filePath).withMode(WriteMode.ADD).uploadAndFinish(fis);

			System.out.println("✅ File uploaded successfully!");
			String storagePath = "https://www.dropbox.com/home" + metadata.getPathLower();

			// **Save File Details in Database**
			FileModel fileModel = new FileModel();
			// fileModel.setFileId(UUID.randomUUID()); // Generate unique UUID?? to put or
			// not?
			fileModel.setFileOutputUploadUrl(storagePath);
			fileModel.setFileName(file.getName());
			fileModel.setFileType(multipartFile.getContentType());

			// Save to DB
			fileRepository.save(fileModel);

			return applicationUtils.generateSuccessResponse("✅ Success: File uploaded successfully!", HttpStatus.OK);
		} catch (IOException e) {
			return applicationUtils.generateErrorResponse("❌ Error: File upload error!", HttpStatus.EXPECTATION_FAILED);
		}
	}
}
