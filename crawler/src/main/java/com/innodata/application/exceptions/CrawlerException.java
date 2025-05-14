package com.innodata.application.exceptions;

/*This defines a custom error type that represents issues in the application, 
 * specifically related to the CrawlerService.
 * */
import org.springframework.http.HttpStatus;

public class CrawlerException extends RuntimeException {

	private HttpStatus status = null;
	
	public CrawlerException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
	
	public HttpStatus getHttpStatus() {
		return status;
	}
}
