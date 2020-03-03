package com.webapp.app.controller;

import com.webapp.app.model.User;
import com.webapp.app.repository.MongoRepositoryImpl;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;


@RestController
public class CounterController extends HttpServlet {

    private final MongoRepositoryImpl mongoRepository;

    private int iHitCounter;

    public CounterController(MongoRepositoryImpl mongoRepository) {
        this.mongoRepository = mongoRepository;
        iHitCounter = 0;
    }

    @GetMapping("/")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userAgent = request.getHeader("User-Agent");

        String remoteAddr = request.getHeader("X-FORWARDED-FOR");

        if (StringUtils.isEmpty(remoteAddr)) {
            remoteAddr = request.getRemoteAddr();
        }

        User user = getUpdatedUserInfo(remoteAddr);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        printStartHTMLTag(out);
        out.println("<center>");
        out.println("Hello World !");
        out.print("<br>");
        out.println("<h3>Welcome to my website !</h3>");
        out.print("<hr>");
        out.println("You are visitor number: " + (++iHitCounter));
        out.print("<br>");
        out.println("IP number: " + remoteAddr);
        out.print("<br>");
        out.println("User Agent: " + userAgent);
        out.print("<br>");
        out.println("count : " + user.getCount());
        out.println("</center>");
        printEndHTMLTag(out);
    }

    private User getUpdatedUserInfo(String remoteAddr) {
        Optional<User> current = mongoRepository.findById(remoteAddr);
        User user = new User(remoteAddr, 1);
        current.ifPresent(u -> user.setCount(u.getCount() + 1));
        mongoRepository.save(user);
        return user;
    }

    private void printEndHTMLTag(PrintWriter out) {
        out.println("</body>");
        out.println("</html>");
    }

    private void printStartHTMLTag(PrintWriter out) {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Hello World</title>");
        out.println("</head>");
        out.println("<body>");
    }

}