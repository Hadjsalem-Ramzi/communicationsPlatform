package com.ts.plateformcommunication.services.Impl;
import com.ts.plateformcommunication.Model.Projet;
import com.ts.plateformcommunication.Repositories.ProjetRepository;
import com.ts.plateformcommunication.services.ProjetService;
import com.ts.plateformcommunication.dto.ProjetDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjetServiceImp  implements ProjetService {
    private ProjetRepository projetRepository;
    private ModelMapper mapper;
    @Autowired
    public ProjetServiceImp( ProjetRepository projetRepository, ModelMapper mapper){
        this.projetRepository= projetRepository;
        this.mapper=mapper;

    }

    @Override
    public ProjetDto save(ProjetDto projetDto) {
        Projet projet = mapper.map(projetDto,Projet.class);
        Projet projet1=projetRepository.save(projet);
        return mapper.map(projet1,ProjetDto.class);
    }

    @Override
    public ProjetDto findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID ne peut pas être null");
        }

        return mapper.map(
                projetRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Cet ID n'a pas été trouvé")),
                ProjetDto.class
        );
    }

    @Override
    public ProjetDto findByName(String name) {
        return mapper.map(Optional.ofNullable(projetRepository.findProjetByName(name)).orElseThrow(()->new NoSuchElementException("Projet Not Found")),ProjetDto.class);
    }

    @Override
    public List<ProjetDto> findAll() {
        List<Projet> projetList =projetRepository.findAll();
        return projetList.stream().map(projet -> mapper.map(projet,ProjetDto.class)).collect(Collectors.toList());
    }

    @Override
    public ProjetDto update(ProjetDto projetDto) {
        Projet projet=mapper.map(projetDto,Projet.class);
        Projet projet1=projetRepository.saveAndFlush(projet);
        return mapper.map(projet1,ProjetDto.class);
    }

    @Override
    public String delete(Long id) {
      projetRepository.deleteById(id);
      return "successfully, project is deleted";
    }
}
