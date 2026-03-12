package com.ts.plateformcommunication.Repositories;

import com.ts.plateformcommunication.Model.Fichier;
import com.ts.plateformcommunication.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FichierRepository extends JpaRepository<Fichier,Long> {
    Fichier findFichierByName(String name);

    List<Fichier> findByUser(User user);

    @Query("select f from Fichier f where f.id=:id")
    List<Fichier> findFichierByUser(@Param("id") Long id);
    ///////////////////////////////////////////////////////////////////////////////////

    @Query("SELECT f from Fichier f where f.user.id=:idU")
    List<Fichier>findAllFiles(@Param("idU") Long idU);

//@Query("select f from  Fichier f order by f.name")






}
