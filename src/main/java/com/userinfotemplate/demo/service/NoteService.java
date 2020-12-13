package com.userinfotemplate.demo.service;

import com.userinfotemplate.demo.entity.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteService {
    //新建一个笔记
    Note createNote(Integer user_id,String save_time,String note_type,String note_content);

    //查询便签
    List<Note> getNoteListByTypeAndUserId(Integer user_id, String note_type);

    //根据便签id查询便签
    Note getNoteById(Integer note_id);
}
