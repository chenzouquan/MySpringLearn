package com.czq.service;


import com.spring.*;

/**
 * @author chenzou'quan
 */
@Component("userService")
@Scope("prototype")
public class UserServiceImpl implements BeanNameAware, InitializingBean ,UserService{

    @Autowire
    private OrderService orderService;

    private String beanName;

    /**
     * 测试方法
     */
    @Override
    public void test(){
        System.out.println(orderService);
    }

    @Override
    public void setBeanName(String name) {
        beanName = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化");
    }
}
