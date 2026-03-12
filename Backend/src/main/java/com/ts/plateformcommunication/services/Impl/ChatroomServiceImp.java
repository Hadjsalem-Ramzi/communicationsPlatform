package com.ts.plateformcommunication.services.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ts.plateformcommunication.Model.ChatRoom;
import com.ts.plateformcommunication.Model.Forum;
import com.ts.plateformcommunication.Repositories.ChatRoomRepository;
import com.ts.plateformcommunication.Repositories.FichierRepository;
import com.ts.plateformcommunication.services.ChatRoomService;
import com.ts.plateformcommunication.dto.ChatroomDto;
import com.ts.plateformcommunication.dto.ForumDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatroomServiceImp implements ChatRoomService {

    private ChatRoomRepository chatRoomRepository;
    private ModelMapper mapper;
    private ForumServiceImp forumServiceImp;

    @Autowired
    private FichierRepository fichierRepository;

    @Autowired
    public ChatroomServiceImp(ChatRoomRepository chatRoomRepository, ModelMapper mapper,ForumServiceImp forumServiceImp) {
        this.chatRoomRepository = chatRoomRepository;
        this.mapper = mapper;
        this.forumServiceImp=forumServiceImp;
    }

    @Override
    public ChatroomDto save(ChatroomDto chatroomDto,Long idForum) {
        ChatRoom chatRoom = mapper.map(chatroomDto, ChatRoom.class);
        ForumDto f =forumServiceImp.findById(idForum);
        Forum f1 =mapper.map(f,Forum.class);
        chatRoom.setForum(f1);
        chatRoomRepository.save(chatRoom);
        return mapper.map(chatRoom, ChatroomDto.class);
    }

    @Override
    public ChatroomDto findById(Long id) {
        return mapper.map(chatRoomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("L'id Not Found")), ChatroomDto.class);
    }

    @Override
    public ChatroomDto findByName(String name) {
        return mapper.map(Optional.ofNullable(chatRoomRepository.findChatRoomByName(name))
                .orElseThrow(() -> new NoSuchElementException("ChatRoom Not Found")), ChatroomDto.class);
    }

    @Override
    public List<ChatroomDto> findAll() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        return chatRooms.stream().map(chatRoom -> mapper.map(chatRoom, ChatroomDto.class)).collect(Collectors.toList());
    }

    @Override
    public ChatroomDto update(ChatroomDto chatroomDto,Long idForum) {
        ChatRoom chatRoom = mapper.map(chatroomDto, ChatRoom.class);
        ForumDto f =forumServiceImp.findById(idForum);
        Forum f1 =mapper.map(f,Forum.class);
        chatRoom.setForum(f1);
        ChatRoom chatRoom1 = chatRoomRepository.saveAndFlush(chatRoom);
        return mapper.map(chatRoom1, ChatroomDto.class);
    }

    @Override
    public String delete(Long id) {
        // Vérifier si la ChatRoom existe
        if (!chatRoomRepository.existsById(id)) {
            throw new NoSuchElementException("ChatRoom non trouvée");
        }

        // Récupérer la chatRoom par son ID
        ChatRoom chatRoom = chatRoomRepository.findById(id).get();

        // Supprimer la chatRoom
        chatRoomRepository.deleteById(id);

        // Créer un objet JSON avec le message de suppression
        Map<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("message", "Suppression réussie, la ChatRoom a été supprimée");

        // Convertir l'objet JSON en chaîne de caractères
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(jsonResponse);
        } catch (JsonProcessingException e) {
            // Gérer l'exception de la conversion JSON
            e.printStackTrace();
            return null; // Ou lancer une exception appropriée
        }
    }




    public List<ChatroomDto> findByForumId(Long idForum){
        if(idForum==null){
            throw  new IllegalArgumentException("l'id ne se trouve pas");
        }
        ForumDto forumDto =forumServiceImp.findById(idForum);
        if(forumDto==null){
            throw  new NoSuchElementException("Forum not Found");
        }
        Forum forum=mapper.map(forumDto,Forum.class);
        List<ChatRoom> chatRoomList=chatRoomRepository.findByForum(forum);
        return chatRoomList.stream().map(chatRoom -> mapper.map(chatRoom,ChatroomDto.class)).collect(Collectors.toList());
   }
}
