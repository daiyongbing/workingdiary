package com.iscas.workingdiary.service;

import com.iscas.workingdiary.bean.User;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {
    void deleteUser(String userName, int userId);

    void updateUser(User user);
}
