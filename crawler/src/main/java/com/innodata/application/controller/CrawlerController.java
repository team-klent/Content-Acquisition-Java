package com.innodata.application.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dropbox.core.v2.files.UploadErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.innodata.application.service.CrawlerService;

@RestController
@RequestMapping("/api")
public class CrawlerController {
	
	@Autowired
	private CrawlerService crawlerService;

	//for file upload, wait for confirmation
	@PostMapping("/register-job-batch-file")
	public ResponseEntity<HashMap<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {

//		try {
//			String fileUrl = crawlerService.uploadFileToDropbox(file);
//			if (fileUrl != null) {
//				return ResponseEntity.ok("✅ File uploaded successfully! Download Link: " + fileUrl);
//			} else {
//				return ResponseEntity.internalServerError().body("❌ File upload failed!");
//			}
//		} catch (IOException | DbxException e) {
//			return ResponseEntity.internalServerError().body("❌ File upload failed: " + e.getMessage());
//		}
        
        ResponseEntity<HashMap<String, Object>> response = crawlerService.uploadFileToDropbox(file);
        return response;
	}

}
