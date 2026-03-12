package com.ts.plateformcommunication.Repositories;
import com.ts.plateformcommunication.Model.Projet;
import com.ts.plateformcommunication.Model.Tache;
import com.ts.plateformcommunication.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface TacheRepository extends JpaRepository<Tache,Long> {
    Tache findTacheByName(String name);
    List<Tache> findByProjet(Projet projet);
    List<Tache> findByUser(User user);

    @Query("select t from Tache t where t.projet.id=:id")
    List<Tache> findTacheByProjet(@Param("id") Long id);
    //*********************************************************


    @Query("select t from Tache t where t.user.id=:id")
    List<Tache> findTacheByUser(@Param("id") Long id);


}
