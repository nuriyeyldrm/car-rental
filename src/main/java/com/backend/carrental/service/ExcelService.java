package com.backend.carrental.service;

import com.backend.carrental.domain.User;
import com.backend.carrental.helper.ExcelHelper;
import com.backend.carrental.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@AllArgsConstructor
@Service
public class ExcelService {

    UserRepository userRepository;

    public ByteArrayInputStream load() {
        List<User> users = userRepository.findAll();

        return ExcelHelper.usersExcel(users);
    }
}
