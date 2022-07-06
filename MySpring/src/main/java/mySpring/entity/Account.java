package mySpring.entity;

import lombok.Data;
import mySpring.Autowire;
import mySpring.Component;
import mySpring.Qualifier;
import mySpring.Value;

/**
 * @author chenzou'quan
 */
@Component
@Data
public class Account {
    @Value("01")
    private Integer id;
    @Value("陈志强")
    private String name;
    @Value("11")
    private Integer age;
    @Autowire
    @Qualifier("order")
    private Order order;
}
