package com.ysh;

import com.ysh.service.MobilePhoneUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class XClassTests {

    @Autowired
    MobilePhoneUserDetailsService mobilePhoneUserDetailsService;

    @Test
    void contextLoads() {
        System.out.println(mobilePhoneUserDetailsService.isExistByMobilePhone("18501307519"));
    }

}
