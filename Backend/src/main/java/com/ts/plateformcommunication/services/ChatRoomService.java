package com.ts.plateformcommunication.services;

import com.ts.plateformcommunication.dto.ChatroomDto;

import java.util.List;

public interface ChatRoomService {

     ChatroomDto save(ChatroomDto chatroomDto,Long idForum);

    ChatroomDto findById(Long id);

    ChatroomDto findByName(String name);

     List<ChatroomDto> findAll();

    ChatroomDto update(ChatroomDto chatroomDto,Long idForum);

    String delete(Long id);

    List<ChatroomDto> findByForumId(Long idForum);
}
