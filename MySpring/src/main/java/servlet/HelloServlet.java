package servlet;

import dao.HelloDao;
import factory.BeanFactory;
import service.HelloService;
import service.impl.HelloServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chenzou'quan
 */
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    private HelloService helloService = (HelloService) BeanFactory.getDao("hello");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().write(this.helloService.findAll().toString());
    }
}
