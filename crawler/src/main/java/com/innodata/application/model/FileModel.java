package com.innodata.application.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "service_crawler")
public class FileModel {

	@Id
	@Column(name="file_id")
	@GeneratedValue(strategy = GenerationType.UUID)// Generates a unique UUID for file_id
	private UUID fileId;

	@Column(name="file_output_upload_url", nullable = false)
	private String fileOutputUploadUrl;

	@Column(name="file_name", nullable = false)
	private String fileName;

	@Column(name="file_type", nullable = false)
	private String fileType;

	// Constructors
	public FileModel() {
	}

	public FileModel(String fileOutputUploadUrl, String fileName, String fileType) {
		this.fileOutputUploadUrl = fileOutputUploadUrl;
		this.fileName = fileName;
		this.fileType = fileType;
	}

	// Getters and Setters
	public UUID getFileId() {
		return fileId;
	}

	public void setFileId(UUID fileId) {
		this.fileId = fileId;
	}

	public String getFileOutputUploadUrl() {
		return fileOutputUploadUrl;
	}

	public void setFileOutputUploadUrl(String fileOutputUploadUrl) {
		this.fileOutputUploadUrl = fileOutputUploadUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	// ToString method
	@Override
	public String toString() {
		return "FileModel{" + "fileId=" + fileId + ", fileOutputUploadUrl='" + fileOutputUploadUrl + '\''
				+ ", fileName='" + fileName + '\'' + ", fileType='" + fileType + '\'' + '}';
	}
}
