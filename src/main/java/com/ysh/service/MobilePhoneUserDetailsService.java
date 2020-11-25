package com.ysh.service;

import com.ysh.mapper.MobilePhoneMapper;
import com.ysh.mapper.UserMapper;
import com.ysh.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MobilePhoneUserDetailsService implements UserDetailsService {

    @Autowired
    MobilePhoneMapper mobilePhoneMapper;

    public MobilePhoneUserDetailsService() {
    }

    @Override
    public UserDetails loadUserByUsername(String mobilePhone) throws UsernameNotFoundException {
        User user = mobilePhoneMapper.loadUserByUserName(mobilePhone);
        if (user == null) {
            throw new UsernameNotFoundException("mobile phone not found!!!");
        }
        user.setRoles(mobilePhoneMapper.getUserRolesByUserId(user.getId()));
        return user;
    }

    public boolean isExistByMobilePhone(String mobilePhone) {
        int count = mobilePhoneMapper.isExistByMobilePhone(mobilePhone);
        if (count != 1) {
            return false;
        }
        return true;
    }
}
