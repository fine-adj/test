package com.userinfotemplate.demo.entity;

import org.springframework.stereotype.Component;

@Component
public class UserRole {
    private Integer user_id;
    private Integer role_id;
    private Integer state;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "user_id=" + user_id +
                ", role_id=" + role_id +
                ", state=" + state +
                '}';
    }
}
