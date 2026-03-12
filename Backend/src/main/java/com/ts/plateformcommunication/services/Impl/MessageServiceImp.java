package com.ts.plateformcommunication.services.Impl;

import com.ts.plateformcommunication.Model.ChatRoom;
import com.ts.plateformcommunication.Model.Message;
import com.ts.plateformcommunication.Repositories.ChatRoomRepository;
import com.ts.plateformcommunication.Repositories.MessageRepository;
import com.ts.plateformcommunication.security.user.User;
import com.ts.plateformcommunication.security.user.UserRepository;
import com.ts.plateformcommunication.security.user.UserServiceImp;
import com.ts.plateformcommunication.services.MessageService;
import com.ts.plateformcommunication.dto.MessageDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageServiceImp implements MessageService {
    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private ModelMapper mapper;
    private ChatRoomRepository chatRoomRepository;
    private ChatroomServiceImp chatroomServiceImp;
    private UserServiceImp userServiceImp;


    @Autowired
    private MessageServiceImp(MessageRepository messageRepository,UserServiceImp userServiceImp,ChatroomServiceImp chatroomServiceImp,ChatRoomRepository chatRoomRepository,UserRepository userRepository,ModelMapper mapper){
          this.messageRepository=messageRepository;
          this.userRepository=userRepository;
          this.mapper=mapper;
          this.chatRoomRepository=chatRoomRepository;
          this.chatroomServiceImp=chatroomServiceImp;
          this.userServiceImp=userServiceImp;
    }

   @Override
    public MessageDto save(MessageDto messageDto, Long idChatRoom, Long idUser) {
        Message message=mapper.map(messageDto,Message.class);

        // Get the currently authenticated user directly from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User sender = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NoSuchElementException("Sender Not Found"));

        User receiver = userRepository.findById(idUser)
                .orElseThrow(() -> new NoSuchElementException("Receiver Not Found"));
        ChatRoom chatRoom = chatRoomRepository.findById(idChatRoom)
                .orElseThrow(() -> new NoSuchElementException("ChatRoom Not Found"));

        // Set associations
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setChatroom(chatRoom);

        Message savedMessage = messageRepository.save(message);


        return mapper.map(savedMessage, MessageDto.class);
    }

    @Override
    public MessageDto findById(Long id) {
        return mapper.map(messageRepository.findById(id).orElseThrow(()->new NoSuchElementException("Lid Not Found")),MessageDto.class);
    }

    @Override
    public MessageDto findByName(String name) {
        return mapper.map(Optional.ofNullable(messageRepository.findMessageByContenu(name))
                .orElseThrow(()->new NoSuchElementException("Message Not Found")),MessageDto.class);
    }

    @Override
    public List<MessageDto> findAll() {
        List<Message> messageList =messageRepository.findAll();
        return messageList.stream().
                map(message -> mapper.map(message,MessageDto.class)).
                collect(Collectors.toList());
    }

   @Override
   public MessageDto update(MessageDto messageDto, Long messageId) {
       // Récupérer le message existant par son ID
       Message existingMessage = messageRepository.findById(messageId)
               .orElseThrow(() -> new NoSuchElementException("Message not found"));

       // Vérifier si l'utilisateur actuel est l'expéditeur du message
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       if (authentication == null || !authentication.isAuthenticated()) {
           throw new IllegalStateException("User not authenticated");
       }

       UserDetails userDetails = (UserDetails) authentication.getPrincipal();
       User authenticatedUser = userRepository.findByUsername(userDetails.getUsername())
               .orElseThrow(() -> new IllegalStateException("User not found"));

       if (!existingMessage.getSender().equals(authenticatedUser)) {
           throw new IllegalStateException("You are not allowed to update this message");
       }

       // Mettre à jour le contenu du message
       existingMessage.setContenu(messageDto.getContenu());

       // Enregistrer le message mis à jour
       Message updatedMessage = messageRepository.saveAndFlush(existingMessage);

       return mapper.map(updatedMessage, MessageDto.class);
   }

    public List<MessageDto> findAllByChatRoomId(Long chatRoomId) {
        // Vérifier si l'id du chatroom est null
        if (chatRoomId == null) {
            throw new IllegalArgumentException("L'id du chatroom ne peut pas être null");
        }

        // Récupérer la liste des messages associés au chatroom
        List<Message> messages = messageRepository.findByChatroom_Id(chatRoomId);

        // Mapper la liste de messages en liste de MessageDto
        return messages.stream().map(message -> mapper.map(message, MessageDto.class)).collect(Collectors.toList());
    }


    @Override
    public String delete(Long id) {
    messageRepository.deleteById(id);
    return "Succefully,message is deleted";
    }


    @Override
    public List<MessageDto> findBySenderAndReceiver(String senderUsername, String receiverUsername) {
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new NoSuchElementException("Sender Not Found"));
        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new NoSuchElementException("Receiver Not Found"));

        List<Message> messages = messageRepository.findBySenderAndReceiver(sender, receiver);

        return messages.stream()
                .map(message -> mapper.map(message, MessageDto.class))
                .collect(Collectors.toList());
    }


}
