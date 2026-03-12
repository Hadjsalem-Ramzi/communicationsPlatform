package com.ts.plateformcommunication.services;

import com.ts.plateformcommunication.dto.MessageDto;

import java.util.List;

public interface MessageService {
   public MessageDto save(MessageDto messageDto,Long idchatroom,Long idr);

   public MessageDto findById(Long id);

   public MessageDto findByName(String name);

   public List<MessageDto> findAll();

   public MessageDto update(MessageDto messageDto,Long idMessage);

   public String delete(Long id);
   List<MessageDto> findAllByChatRoomId(Long chatRoomId);

   List<MessageDto> findBySenderAndReceiver(String senderUsername, String receiverUsername);


}
