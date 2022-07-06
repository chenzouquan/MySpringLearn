package service.impl;

import dao.HelloDao;
import service.HelloService;

import java.util.Arrays;
import java.util.List;

/**
 * HelloService接口实现类
 * @author chenzou'quan
 */
public class HelloService2Impl implements HelloService {

    private HelloDao helloDao;

    @Override
    public List<String> findAll() {
        return Arrays.asList("7","8","9");
    }
}
