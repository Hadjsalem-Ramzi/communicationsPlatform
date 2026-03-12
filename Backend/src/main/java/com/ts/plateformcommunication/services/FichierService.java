package com.ts.plateformcommunication.services;
import com.ts.plateformcommunication.dto.FichierDto;

import java.util.List;


public interface FichierService {
    public FichierDto save(FichierDto fichierDto,Long idUser);

    public FichierDto findById(Long id);

    public FichierDto findByName(String name);

    public List<FichierDto> findAll();

    public FichierDto update(FichierDto fichierDto,Long idFichier,Long idUser);

    String delete(Long id);

    List<FichierDto> findByUserId(Long idUser);

    List<FichierDto>findAllFiles(Long idU);



}
