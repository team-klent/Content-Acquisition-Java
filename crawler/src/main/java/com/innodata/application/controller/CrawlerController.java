package com.innodata.application.controller;

/*This is a controller class for handling file uploads. When file is sent to 
 * API *WAIT FOR CONFIRMATION* endpoint, system processes the file and uploads
 * it to DropBox. 
 * */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innodata.application.exceptions.CrawlerException;
import com.innodata.application.model.FileModel;
import com.innodata.application.repository.FileRepository;
import com.innodata.application.service.CrawlerService;
import com.innodata.application.utilities.ApplicationUtils;
import com.innodata.application.utilities.EncodingUtils;

@RestController
@RequestMapping("/api")
public class CrawlerController {

	// Application Utilities
	@Autowired
	private ApplicationUtils applicationUtils;

	// Crawler Service Layer
	@Autowired
	private CrawlerService crawlerService;

	// For file upload, wait for confirmation whether rename or follow Rifky's docs
	@PostMapping("/upload-file")
	public ResponseEntity<HashMap<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {

		FileModel fileData = crawlerService.uploadFileToDropbox(file);
		String fileUrl = fileData.getFileOutputUploadUrl();
		return applicationUtils.generateUploadResponse("✓ Success: File upload and registration success!", fileUrl);
	}

	@GetMapping("/get-all-files")
	public ResponseEntity<HashMap<String, Object>> getAllFiles() {
		
		List<FileModel> files = crawlerService.getAllFiles();
		return applicationUtils.generateGetFilesResponse("✓ Success: Files retrieved!", files);
	}
}
