package com.backend.carrental.repository;

import com.backend.carrental.domain.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FileDBRepository extends JpaRepository<FileDB, String> {
}
