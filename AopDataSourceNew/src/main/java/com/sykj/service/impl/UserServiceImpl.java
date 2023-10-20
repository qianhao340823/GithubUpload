package com.sykj.service.impl;

import com.sykj.entity.User;
import com.sykj.mapper.UserMapper;
import com.sykj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public List<User> findAllUser() {
        List<User> list = userMapper.findAllUser();
        System.out.println(list.toString());
        return list;
    }

}
