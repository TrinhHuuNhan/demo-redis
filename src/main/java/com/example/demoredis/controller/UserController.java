package com.example.demoredis.controller;


import com.example.demoredis.common.Constants;
import com.example.demoredis.model.User;
import com.example.demoredis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping(value = "/users")
    public List<User> getAllUser() {
        return userService.getAllUser();
    }

    @PostMapping(value = "/users")
    @ExceptionHandler
    @Transactional(rollbackOn = Exception.class)
    public String deleteCache(){
        redisTemplate.delete(Constants.GET_ALL_USER);
        return "Delete Cache Success";
    }
}
