package com.ts.plateformcommunication.services.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ts.plateformcommunication.Model.ChatRoom;
import com.ts.plateformcommunication.Model.Forum;
import com.ts.plateformcommunication.Model.Message;
import com.ts.plateformcommunication.Repositories.ChatRoomRepository;
import com.ts.plateformcommunication.Repositories.ForumRepository;
import com.ts.plateformcommunication.Repositories.MessageRepository;
import com.ts.plateformcommunication.services.ForumService;
import com.ts.plateformcommunication.dto.ForumDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ForumServiceImp implements ForumService {
    private ForumRepository forumRepository;
    private ChatRoomRepository chatRoomRepository;
    private ModelMapper mapper;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ForumServiceImp(ForumRepository forumRepository, ModelMapper mapper, ChatRoomRepository chatRoomRepository) {
        this.forumRepository = forumRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.mapper = mapper;
    }


    @Override
    public ForumDto save(ForumDto forumDto) {
        Forum forum = mapper.map(forumDto, Forum.class);
        Forum forum1 = forumRepository.save(forum);
        return mapper.map(forum1, ForumDto.class);
    }

    @Override
    public ForumDto findById(Long id) {
        return mapper.map(forumRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Id Not Found")), ForumDto.class);
    }

    @Override
    public ForumDto findByName(String name) {
        return mapper.map(Optional.ofNullable(forumRepository.findForumByName(name)).orElseThrow(() -> new NoSuchElementException("Forum Not Found")), ForumDto.class);
    }

    @Override
    public List<ForumDto> findAll() {
        List<Forum> forumList = forumRepository.findAll();
        return forumList.stream().map(forum -> mapper.map(forum, ForumDto.class)).collect(Collectors.toList());
    }

    @Override
    public ForumDto update(ForumDto forumDto) {
        Forum forum = mapper.map(forumDto, Forum.class);
        Forum forum1 = forumRepository.saveAndFlush(forum);
        return mapper.map(forum1, ForumDto.class);
    }

    public String delete(Long id) {
        // Récupérer le forum par ID
        Forum forum = forumRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Forum non trouvé"));

        // Récupérer les chatrooms associés
        List<ChatRoom> chatRooms = forum.getChatroom();
        if (chatRooms != null) {
            for (ChatRoom chatRoom : chatRooms) {
                // Récupérer les messages associés à chaque chatroom
                List<Message> messages = chatRoom.getMessage();
                if (messages != null) {
                    // Supprimer chaque message associé
                    if (messageRepository != null) {
                        for (Message message : messages) {
                            messageRepository.deleteById(message.getId());
                        }
                    } else {
                        // Handle the case where messageRepository is null
                        // Log an error or throw an exception depending on your error handling strategy
                        // For example:
                        throw new IllegalStateException("messageRepository is null");
                    }
                }
                // Supprimer le chatroom
                if (chatRoomRepository != null) {
                    chatRoomRepository.deleteById(chatRoom.getId());
                } else {
                    // Handle the case where chatRoomRepository is null
                    // Log an error or throw an exception depending on your error handling strategy
                    // For example:
                    throw new IllegalStateException("chatRoomRepository is null");
                }
            }
        }

        // Supprimer le forum
        if (forumRepository != null) {
            forumRepository.deleteById(id);
        } else {
            // Handle the case where forumRepository is null
            // Log an error or throw an exception depending on your error handling strategy
            // For example:
            throw new IllegalStateException("forumRepository is null");
        }

        // Créer un objet JSON avec le message de suppression
        Map<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("message", "Suppression réussie, le forum a été supprimé");

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



}


