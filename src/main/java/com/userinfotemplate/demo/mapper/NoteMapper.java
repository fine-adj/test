package com.userinfotemplate.demo.mapper;

import com.userinfotemplate.demo.entity.Note;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("NoteMapper")
public interface NoteMapper {

    //新建一个笔记
    Note createNote(Integer user_id,String save_time,String note_type,String note_content);

    //查询便签
    List<Note> getNoteListByTypeAndUserId(Integer user_id,String note_type);

    //根据便签id查询便签
    Note getNoteById(Integer note_id);


}
