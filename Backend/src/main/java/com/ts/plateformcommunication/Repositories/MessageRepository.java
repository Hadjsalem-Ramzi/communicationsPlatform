package com.ts.plateformcommunication.Repositories;

import com.ts.plateformcommunication.Model.Message;
import com.ts.plateformcommunication.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    Message findMessageByContenu(String contenu);

    List<Message> findByChatroom_Id(Long chatroomId);

    @Query("SELECT m FROM Message m WHERE m.sender = :sender OR m.receiver = :receiver")
    List<Message> findBySenderOrReceiver(@Param("sender") User sender, @Param("receiver") User receiver);

    List<Message> findBySenderAndReceiver(User sender, User receiver);


}
