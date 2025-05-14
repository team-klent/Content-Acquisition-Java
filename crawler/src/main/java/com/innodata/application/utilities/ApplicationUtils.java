package com.innodata.application.utilities;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innodata.application.model.FileModel;

@Service
public class ApplicationUtils {

	@Value("${project.code}")
	private String PROJECT_CODE;
	
	@Value("${workflow.code}")
	private String WORKFLOW_CODE;
	
	@Value("${content.acquisition.task.uid}")
	private String TASK_UID;
    
    public ResponseEntity<HashMap<String, Object>> generateErrorResponse(String errMessage, HttpStatus status) {
    	HashMap<String, Object> resBody = generateCommonResBody();
		resBody.put("message", errMessage);
    	HttpHeaders headers = generateHeaders();
    	
    	ResponseEntity<HashMap<String, Object>> errResponse = new ResponseEntity<HashMap<String, Object>>(resBody, headers, status);
    	return errResponse;
    }
    
    public ResponseEntity<HashMap<String, Object>> generateUploadResponse(String successMessage, Object data) {
		
		HashMap<String, Object> resBody = generateCommonResBody();
		resBody.put("message", successMessage);
		resBody.put("data", data);
    	resBody.put("url", generateURL(resBody));
    	HttpHeaders headers = generateHeaders();
    	
    	ResponseEntity<HashMap<String, Object>> successResponse = new ResponseEntity<HashMap<String, Object>>(resBody, headers, HttpStatus.OK);
    	return successResponse;
    }
    
    public ResponseEntity<HashMap<String, Object>> generateGetFilesResponse(String successMessage, List<FileModel> data) {
		
		HashMap<String, Object> resBody = generateCommonResBody();
		resBody.put("message", successMessage);
		resBody.put("data", data);
    	HttpHeaders headers = generateHeaders();
    	
    	ResponseEntity<HashMap<String, Object>> successResponse = new ResponseEntity<HashMap<String, Object>>(resBody, headers, HttpStatus.OK);
    	return successResponse;
    }
    
    private HashMap<String, Object> generateCommonResBody() {
    	HashMap<String, Object> resBody = new HashMap<String, Object>();
    	resBody.put("project_code", PROJECT_CODE);
    	resBody.put("workflow_code", WORKFLOW_CODE);
    	resBody.put("first_task_uid", TASK_UID);
		
		return resBody;
    }
    
    private String generateURL(HashMap<String, Object> resBody) {
    	String finalUrl = "-";
    	try {
	    	ObjectMapper objectMapper = new ObjectMapper();
	        String jsonString = objectMapper.writeValueAsString(resBody);
	        String encoded = URLEncoder.encode(jsonString, StandardCharsets.UTF_8);
	        finalUrl = "https://www.localhost.com/api/upload-file?filter=" + encoded;
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    	System.out.println(finalUrl);
    	return finalUrl;
    }
    
    private HttpHeaders generateHeaders() {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
