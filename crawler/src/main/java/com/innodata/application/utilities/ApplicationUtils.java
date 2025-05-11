package com.innodata.application.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ApplicationUtils {

	@Value("${project.code}")
	private String PROJECT_CODE;
	
	@Value("${workflow.code}")
	private String WORKFLOW_CODE;
	
	@Value("${content.acquisition.task.uid}")
	private String TASK_UID;
	
	@Value("${ia.api.token}")
	private String API_TOKEN;
	
    public File fileConversion(MultipartFile multipartFile) throws IOException {
        File convertedFile = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(multipartFile.getBytes());
        }
        return convertedFile;
    }
    
    public ResponseEntity<HashMap<String, Object>> generateErrorResponse(String errMessage, HttpStatus status) {
    	HashMap<String, Object> data = new HashMap<String, Object>();
    	data.put("message", errMessage);
    	
    	HttpHeaders headers = generateHeaders();
    	ResponseEntity<HashMap<String, Object>> errResponse = new ResponseEntity<HashMap<String, Object>>(data, headers, status);
    	return errResponse;
    }
    
    public ResponseEntity<HashMap<String, Object>> generateSuccessResponse(String successMessage, HttpStatus status) throws JsonProcessingException {
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("message", successMessage);
		data.put("project_code", PROJECT_CODE);
		data.put("workflow_code", WORKFLOW_CODE);
		data.put("first_task_uid", TASK_UID);
		data.put("file_unique_identifier", "unique");
		data.put("file_name", "unique.txt");
		data.put("file_path", "-");

		Map<String, String> metaData = new HashMap<>();
		metaData.put("M1", "V1");
		metaData.put("M2", "V2");
		ObjectMapper objectMapper = new ObjectMapper();
		String metaDataJson = objectMapper.writeValueAsString(metaData);

		data.put("meta_data", metaDataJson);
    	
    	HttpHeaders headers = generateHeaders();
    	ResponseEntity<HashMap<String, Object>> successResponse = new ResponseEntity<HashMap<String, Object>>(data, headers, status);
    	return successResponse;
    }
    
    private HttpHeaders generateHeaders() {
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_TOKEN);
        return headers;
    }
}
