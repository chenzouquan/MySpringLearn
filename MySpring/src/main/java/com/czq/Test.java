package com.czq;

import com.czq.service.UserService;
import com.czq.service.UserServiceImpl;
import com.spring.CzqApplicationContext;

/**
 * @author chenzou'quan
 */
public class Test {
    public static void main(String[] args) {
        CzqApplicationContext applicationContext = new CzqApplicationContext(AppConfig.class);

        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.test();

    }
}
