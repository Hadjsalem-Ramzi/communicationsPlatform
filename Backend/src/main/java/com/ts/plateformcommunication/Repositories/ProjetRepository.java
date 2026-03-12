package com.ts.plateformcommunication.Repositories;

import com.ts.plateformcommunication.Model.Projet;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjetRepository extends JpaRepository<Projet,Long> {
    Projet findProjetByName(String name);
}
