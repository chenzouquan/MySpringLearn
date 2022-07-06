package com.spring;

import com.czq.AppConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 容器单元测试类
 */
class CzqApplicationContextTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void getBean() {
        CzqApplicationContext applicationContext = new CzqApplicationContext(AppConfig.class);
        applicationContext.getBean("userService");
    }
}