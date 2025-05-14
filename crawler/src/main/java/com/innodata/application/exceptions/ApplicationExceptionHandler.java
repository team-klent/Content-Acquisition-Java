package com.innodata.application.exceptions;

//CHELLE's instruction to include responses
/*This is responsible for catching errors that occur in the application and returning helpful messages to users. 
 *This organizes the message into a clear response, so users would understand.
 * */

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.innodata.application.utilities.ApplicationUtils;

@ControllerAdvice
public class ApplicationExceptionHandler {

	// Application Utilities
	@Autowired
	private ApplicationUtils applicationUtils;
	
	@ExceptionHandler(CrawlerException.class)
    public ResponseEntity<HashMap<String, Object>> handleCrawlerException(CrawlerException ex) {
		return applicationUtils.generateErrorResponse(ex.getMessage(), ex.getHttpStatus());
    }
}
