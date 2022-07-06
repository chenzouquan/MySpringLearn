package service;

import dao.HelloDao;

import java.util.List;

/**
 * @author chenzou'quan
 */
public interface HelloService {
    /**
     * find All 测试方法
     * @return 返回查找到的列表
     */
    public List<String> findAll();
}
