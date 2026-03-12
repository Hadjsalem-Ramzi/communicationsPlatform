package com.ts.plateformcommunication.services;

import com.ts.plateformcommunication.dto.ProjetDto;

import java.util.List;

public interface ProjetService {
    ProjetDto save (ProjetDto projetDto);

     ProjetDto findById(Long id);

    ProjetDto findByName(String name);

    List<ProjetDto> findAll();

    ProjetDto update (ProjetDto projetDto);

    String delete (Long id);
}
