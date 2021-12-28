package com.example.demoredis.service;

import com.example.demoredis.common.Constants;
import com.example.demoredis.model.User;
import com.example.demoredis.repo.UserRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private Gson gson;

    //private ObjectMapper objectMapper;

    @PostConstruct
    public void init(){
        //objectMapper = new ObjectMapper();
        gson = new Gson();
    }

    public List<User> getAllUser(){
        long startTime = System.currentTimeMillis();
        String dataFrom;
        List<User> users;

        String redisResult = redisTemplate.opsForValue().get(Constants.GET_ALL_USER);
        System.out.println("Is Cached: " + ((redisResult == null) ? "No" : "Yes") );

        if(redisResult == null){
            dataFrom = "MYSQL";
            users = userRepository.findAll();
            //objectMapper.read
            //String data = objectMapper.writeValueAsString(users);
            redisTemplate.opsForValue().set(Constants.GET_ALL_USER,gson.toJson(users));
            redisTemplate.expire(Constants.GET_ALL_USER,30, TimeUnit.SECONDS);
        }else{
            dataFrom = "REDIS_CACHE";
            users = gson.fromJson(redisResult, new TypeToken<List<User>>(){}.getType());
        }

        System.out.println("USER-SERVICE------GET-LIST-USERS-----DATA FROM " + dataFrom);
        System.out.println("TIME : " + (System.currentTimeMillis() - startTime));
        return users;
    }

}
