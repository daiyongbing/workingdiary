package com.iscas.workingdiary.service;

import com.iscas.workingdiary.bean.Diary;
import com.iscas.workingdiary.bean.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {
    void deleteUser(String userName, String userId);

    void updateUser(User user);

    void resetPassword(String userName);

    List<User> selectAllUser();

    List<Diary> selectDiaryByName(String userName);
}
