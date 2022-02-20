package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.model.ImageUploaded;

@Repository
public interface ImageUploadRepository extends JpaRepository<ImageUploaded, Integer> {

}
