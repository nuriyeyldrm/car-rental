package com.backend.carrental.repository;

import com.backend.carrental.domain.User;
import com.backend.carrental.exception.ConflictException;
import com.backend.carrental.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username) throws ResourceNotFoundException;

    Boolean existsByUsername(String username) throws ConflictException;

    Boolean existsByEmail(String email) throws ConflictException;

}
