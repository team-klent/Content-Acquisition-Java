package com.innodata.application.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.innodata.application.model.FileModel;

@Repository
public interface FileRepository extends JpaRepository<FileModel, UUID> {
	
}
