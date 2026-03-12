package com.ts.plateformcommunication.Repositories;
import com.ts.plateformcommunication.Model.ChatRoom;
import com.ts.plateformcommunication.Model.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    Optional<ChatRoom> findChatRoomByName(String name);

    List<ChatRoom>findByForum(Forum forum);

    @Query("select f from Forum  f where f.id=:id")
    List<ChatRoom>findChatRoomByForum(@Param("id") Long id);
}
