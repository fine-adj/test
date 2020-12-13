package com.userinfotemplate.demo.entity;

import org.springframework.stereotype.Component;

@Component
public class Note {
    private Integer note_id;
    private String save_time;
    private String note_type;
    private String note_content;
    private Integer user_id;

    public Integer getNote_id() {
        return note_id;
    }

    public void setNote_id(Integer note_id) {
        this.note_id = note_id;
    }

    public String getSave_time() {
        return save_time;
    }

    public void setSave_time(String save_time) {
        this.save_time = save_time;
    }

    public String getNote_type() {
        return note_type;
    }

    public void setNote_type(String note_type) {
        this.note_type = note_type;
    }

    public String getNote_content() {
        return note_content;
    }

    public void setNote_content(String note_content) {
        this.note_content = note_content;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Note{" +
                "note_id=" + note_id +
                ", save_time='" + save_time + '\'' +
                ", note_type='" + note_type + '\'' +
                ", note_content='" + note_content + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
