package com.innodata.application.model;

/*Model / entity class.
 * This represents the file stored in the system, with details like name, type, and UUID. 
 * This too tracks where the file is uploaded. 
 * */

import jakarta.persistence.*;

import java.util.Map;
import java.util.UUID;

import com.innodata.application.utilities.EncodingUtils;

@Entity
@Table(name = "service_crawler_table")
public class FileModel {

	@Id
	@Column(name = "file_id", unique = true, nullable = false)
	private UUID fileId;

	@Column(name = "file_name", nullable = false)
	private String fileName;

	@Column(name = "file_type", nullable = false)
	private String fileType;

	@Column(name = "file_output_upload_url", nullable = false)
	private String fileOutputUploadUrl;

	// Constructors
	public FileModel() {
		this.fileId = UUID.randomUUID();
	}

	public FileModel(String fileOutputUploadUrl, String fileName, String fileType) {
		this.fileId = UUID.randomUUID();
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

	public String getFileOutputUploadUrl() {
		return fileOutputUploadUrl;
	}

	public void setFileOutputUploadUrl(String fileOutputUploadUrl) {
		this.fileOutputUploadUrl = fileOutputUploadUrl;
	}

	// ToString method
	@Override
	public String toString() {
		return "FileModel{" + "fileId=" + fileId + ", fileOutputUploadUrl='" + fileOutputUploadUrl + '\''
				+ ", fileName='" + fileName + '\'' + ", fileType='" + fileType + '\'' + '}';
	}

	/*public String toUrlParams() {
		Map<String, String> params = Map.of("fileId", fileId.toString(), "fileName", fileName, "fileType", fileType,
				"fileOutputUploadUrl", fileOutputUploadUrl);
		return UrlUtils.toQueryString(params);
	}*/
}
