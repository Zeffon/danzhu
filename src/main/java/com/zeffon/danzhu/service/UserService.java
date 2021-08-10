package com.zeffon.danzhu.service;

import com.zeffon.danzhu.bo.WxInfoBO;
import com.zeffon.danzhu.core.LocalUser;
import com.zeffon.danzhu.model.User;
import com.zeffon.danzhu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Create by Zeffon on 2020/10/1
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(Integer id) {
        return userRepository.findFirstById(id);
    }

    public User getUserById() {
        return LocalUser.getUser();
    }

    @Transactional
    public void updateUserWxInfo(WxInfoBO wxInfo) {
        User user = LocalUser.getUser();
        user.setWxInfo(wxInfo);
        this.userRepository.save(user);
    }
}
