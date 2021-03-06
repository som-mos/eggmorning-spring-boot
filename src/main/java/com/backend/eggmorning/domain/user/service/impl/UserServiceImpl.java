package com.backend.eggmorning.domain.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.eggmorning.domain.user.entity.User;
import com.backend.eggmorning.domain.user.repository.UserRepository;
import com.backend.eggmorning.domain.user.service.inf.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

    private UserRepository usersRepository;

    public UserServiceImpl(UserRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    @Override
    public void createUser(User user) {
        this.usersRepository.save(user);
    }

    public User getUserByName(String username) throws UsernameNotFoundException {
        return this.usersRepository.findByName(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return this.usersRepository.findByEmail(email);
    }
}
