package com.softlaboratory.storageservice.repository;

import com.softlaboratory.storageservice.domain.dao.FileDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileDao, Long> {
}
