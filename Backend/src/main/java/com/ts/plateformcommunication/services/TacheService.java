package com.ts.plateformcommunication.services;

import com.ts.plateformcommunication.Model.Tache;
import com.ts.plateformcommunication.dto.TacheDto;

import java.util.List;

public interface TacheService {
    TacheDto save(TacheDto tacheDto,Long idProjet,Long idUser);

    TacheDto findById(Long id);

    TacheDto findByName(String name);

    List<Tache> findAll();

    TacheDto update(TacheDto tacheDto,Long id,Long idP,Long idU);

    String delete(Long id);

    List<TacheDto> findByProjetId(Long projet_id);

    List<TacheDto> findByUserId(Long User_id);
}
