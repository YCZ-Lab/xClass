package com.ysh.service;

import com.ysh.mapper.UserMapper;
import com.ysh.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    public UserService() {
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("account not found!!!");
        }
        user.setRoles(userMapper.getUserRolesByUserId(user.getId()));
        return user;
    }

    public void save(User user) {
        userMapper.save(user);
    }

    public void setRole(int id) {
        userMapper.setRole(id);
    }
}
