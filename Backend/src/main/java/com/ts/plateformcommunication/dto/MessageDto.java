package com.ts.plateformcommunication.dto;

import com.ts.plateformcommunication.Model.ChatRoom;
import com.ts.plateformcommunication.security.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long id;
    private String contenu;
    private User sender; // Nom du sender
    private User receiver; // Nom du receiver
    private ChatRoom chatroom;
}
