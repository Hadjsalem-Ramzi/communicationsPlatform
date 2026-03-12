package com.ts.plateformcommunication.Repositories;
import com.ts.plateformcommunication.Model.Forum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRepository extends JpaRepository<Forum,Long> {
    Forum findForumByName(String name);


}
