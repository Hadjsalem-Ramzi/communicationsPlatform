package com.ts.plateformcommunication.services.Impl;
import com.ts.plateformcommunication.Model.Fichier;
import com.ts.plateformcommunication.Repositories.FichierRepository;
import com.ts.plateformcommunication.dto.UserDto;
import com.ts.plateformcommunication.security.user.User;
import com.ts.plateformcommunication.security.user.UserServiceImp;
import com.ts.plateformcommunication.services.FichierService;
import com.ts.plateformcommunication.dto.FichierDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FichierServiceImp  implements FichierService {
    private FichierRepository fichierRepository;
    private UserServiceImp userServiceImp;
    private ModelMapper mapper;

    @Autowired
    public FichierServiceImp(FichierRepository fichierRepository, ModelMapper mapper,UserServiceImp userServiceImp) {
        this.fichierRepository = fichierRepository;
        this.userServiceImp =userServiceImp;
        this.mapper = mapper;
    }


    @Override
    public FichierDto save(FichierDto fichierDto,Long idUser) {
        Fichier fichier=mapper.map(fichierDto,Fichier.class);
        //Fichier fichier1=fichierRepository.save(fichier);
        UserDto e =this.userServiceImp.findById(idUser);
        User u1 = mapper.map(e,User.class);
        fichier.setUser(u1);
        fichierRepository.save(fichier);
        return mapper.map(fichier,FichierDto.class);
    }



    @Override
    public FichierDto findById(Long id) {
        return mapper.map(fichierRepository.findById(id).orElseThrow(()->new NoSuchElementException("Id Not Found")),FichierDto.class);
    }

    @Override
    public FichierDto findByName(String name)
    {
        return mapper.map(Optional.ofNullable(fichierRepository.findFichierByName(name))
                .orElseThrow(()->new NoSuchElementException("Fichier Not Found")),FichierDto.class);
    }

    @Override
    public List<FichierDto> findAll() {
        List<Fichier>fichierList =fichierRepository.findAll();
       return fichierList.stream().map(fichier -> mapper.map(fichier,FichierDto.class)).collect(Collectors.toList());
    }

    @Override
    public FichierDto update(FichierDto fichierDto,Long idF,Long idU) {
        Fichier fichier=mapper.map(fichierDto,Fichier.class);
        fichier.setUser(mapper.map(userServiceImp.findById(idU),User.class));
        fichier.setId(idF);
        Fichier fichier1=fichierRepository.saveAndFlush(fichier);
        return mapper.map(fichier1,FichierDto.class);
    }

    @Override
    public String delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }
      fichierRepository.deleteById(id);
      return "succefully,Fichier is deleted";
    }

    @Override
    public List<FichierDto> findByUserId(Long user_id) {
        if(user_id==null){
            throw new IllegalArgumentException("ID de user ne peut pas être null");
        }
        UserDto userDto=userServiceImp.findById(user_id);
        if(user_id==null){
            throw new NoSuchElementException("Employee non trouvé");
        }
       User user=mapper.map(userDto,User.class);
        List<Fichier> fichierList= fichierRepository.findByUser(user);
        return fichierList.stream().map(fichier -> mapper.map(fichier,FichierDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<FichierDto> findAllFiles(Long idU){
        if(idU == null){
            throw  new NoSuchElementException("Id user not found");
        }

        List<Fichier> fichierList = fichierRepository.findAllFiles(idU);

        return fichierList.stream().map(fichier->mapper.map(fichier,FichierDto.class )).collect(Collectors.toList());

    }




    public List<FichierDto> findAllOrderedByName() {
        List<Fichier> fichierList = fichierRepository.findAll();

        // Trier la liste des fichiers par leur nom
        fichierList.sort(Comparator.comparing(Fichier::getName));

        // Mapping de Fichier à FichierDto
        return fichierList.stream()
                .map(fichier -> mapper.map(fichier, FichierDto.class))
                .collect(Collectors.toList());
    }


}