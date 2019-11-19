package com.chenmingjun.test;

import com.chenmingjun.service.UserService;
import com.chenmingjun.service.UserServiceImpl;
import org.spring.util.BeanFactory;

public class Test {

    public static void main(String[] args) {
        BeanFactory beanFactory = new BeanFactory("spring.xml");
        UserService service =(UserService) beanFactory.getBean("service");

        service.find();
    }
}
