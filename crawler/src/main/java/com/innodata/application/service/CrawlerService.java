package com.innodata.application.service;

/*This class is responsible for handling file uploads in a Spring Boot application and storing them in Dropbox.
 * */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.innodata.application.exceptions.CrawlerException;
import com.innodata.application.model.FileModel;
import com.innodata.application.repository.FileRepository;

@Service
public class CrawlerService {

	@Value("${dropbox.access.token}")
	private String ACCESS_TOKEN;

	// JPA Repository
	@Autowired
	private FileRepository fileRepository;

	@Transactional
	public FileModel uploadFileToDropbox(MultipartFile multipartFile) {
		
		// Multipart file converson to File object
		// Currently implemented assuming that front end will send a multipartFile
		File file = fileConversion(multipartFile);

		if (ACCESS_TOKEN == null || ACCESS_TOKEN.isEmpty()) {
			throw new CrawlerException("❌ Error: Dropbox Access Token is not set!", HttpStatus.BAD_REQUEST);
		}

		// Dropbox client instantiation
		DbxRequestConfig config = new DbxRequestConfig("JavaDropboxApp");
		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

		// Generate a timestamped folder name
		String folderName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String folderPath = "/" + folderName;

		// File upload process
		String filePath = folderPath + "/" + file.getName();
		try (FileInputStream fis = new FileInputStream(file)) {
			
			// Create the folder in Dropbox
			FolderMetadata folderMetadata = client.files().createFolderV2(folderPath).getMetadata();
			System.out.println("✅ Folder created successfully: " + folderMetadata.getPathLower());
			
			// Upload file to folder in Dropbox
			FileMetadata metadata = client.files().uploadBuilder(filePath).withMode(WriteMode.ADD).uploadAndFinish(fis);
			System.out.println("✅ File uploaded successfully!");
			
			// Acquire Dropbox storage path
			String storagePath = "https://www.dropbox.com/home" + metadata.getPathLower();

			// FileModel instantiation
			FileModel fileModel = new FileModel();
			fileModel.setFileName(file.getName());
			fileModel.setFileType(multipartFile.getContentType());
			fileModel.setFileOutputUploadUrl(storagePath);

			// Save to DB
			return fileRepository.save(fileModel);
			
		} catch (DbxException e) {
			// Folder creation error
			throw new CrawlerException("❌ Error: Folder creation failed!", HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			// File upload error
			throw new CrawlerException("❌ Error: File upload failed!", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			// Other errors
			throw new CrawlerException("❌ Error: File upload or file registration failed", HttpStatus.BAD_REQUEST);
		}
	}
	
	@Transactional
	public List<FileModel> getAllFiles() {
		
		try {
			List<FileModel> files = fileRepository.findAll();
			
			// Check if there are any files saved in the database
			if (files == null || files.isEmpty()) {
				System.out.println("❌ No files in the database");
				throw new CrawlerException("❌ Error: There are no files registered!", HttpStatus.BAD_REQUEST);
			}
			
			System.out.println(files.size() + " files in the database");
			return files;
			
		} catch (Exception e) {
			// Errors during fetching of all files
			throw new CrawlerException("❌ Error: File acquisition error!", HttpStatus.BAD_REQUEST);
		}
	}
	
    private File fileConversion(MultipartFile multipartFile) {
        File convertedFile = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
        	throw new CrawlerException("❌ Error: File Conversion Failed", HttpStatus.BAD_REQUEST);
        }
        return convertedFile;
    }
    
    
//    public String generateEncodedUrl(Map<String, Object> filterParams) {
//        try {
//            String jsonString = objectMapper.writeValueAsString(filterParams);
//            String encodedJson = EncodingUtils.encodeJson(jsonString);
//            return ApplicationUtils.buildEncodedUrl(encodedJson);
//        } catch (Exception e) {
//            throw new RuntimeException("Error encoding filter parameters", e);
//        }
//    }
}
