package com.userinfotemplate.demo.service.serverImpl;

import com.userinfotemplate.demo.entity.Note;
import com.userinfotemplate.demo.mapper.NoteMapper;
import com.userinfotemplate.demo.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteMapper noteMapper;


    //新建一个笔记
    @Override
    public Note createNote(Integer user_id,String save_time,String note_type,String note_content){
        return noteMapper.createNote(user_id,save_time,note_type,note_content);
    }

    //查询便签
    @Override
    public List<Note> getNoteListByTypeAndUserId(Integer user_id, String note_type){
        return noteMapper.getNoteListByTypeAndUserId(user_id,note_type);
    }

    //根据便签id查询便签
    @Override
    public Note getNoteById(Integer note_id){
        return noteMapper.getNoteById(note_id);
    }

}
