package com.smartbank.service;

import com.smartbank.model.Users;
import com.smartbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
{

    @Autowired
    UserRepository userRepository;



    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public String login(String username, String password)
    {
        Users users =userRepository.findByUsername(username);
        if(users != null && users.getPassword().equals(password))
        {
            return "Login successful! Welcome "+ users.getUsername();
        }
        else
        {
            return "Invalid Credentials";
        }
    }

    public String Signin(String username, String password , String email)
    {
        Users users1 =userRepository.findByUsername(username);
        if(users1 ==null)
        {

        Users users = new Users();
        users.setUsername(username);
        users.setEmail(email);
        users.setPassword(password);
        userRepository.save(users);
        return "Account Created";
        }
        else {
            return "Use Different Username";
        }
    }


}
