package com.webapp.app.controller;

import com.webapp.app.model.User;
import com.webapp.app.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;


@RestController
public class CounterServlet extends HttpServlet {
    @Autowired
    private RedisRepository redisRepository;
    private int iHitCounter;

    public void init() throws ServletException {
        iHitCounter = 0;
    }

    @GetMapping("/")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
            User current = redisRepository.findUser(remoteAddr);
            User user = new User(remoteAddr, Objects.nonNull(current) ? current.getCount() + 1 : 1);
            redisRepository.add(user);

            PrintWriter out = response.getWriter();
            out.println("Hello World !");
            out.println("Welcome to my website !");
            out.println("IP number: " + (remoteAddr));
            out.println("count : " + (user.getCount()));
        }

    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}