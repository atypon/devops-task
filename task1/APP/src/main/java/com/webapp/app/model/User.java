package com.webapp.app.model;

import org.springframework.data.annotation.Id;

import java.io.Serializable;


public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private int count;

    public User(String id, int count) {
        this.id = id;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", count=" + count +
                '}';
    }
}
