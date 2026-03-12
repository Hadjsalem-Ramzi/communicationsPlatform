package com.ts.plateformcommunication.services.Impl;
import com.ts.plateformcommunication.Model.Projet;
import com.ts.plateformcommunication.Model.Tache;
import com.ts.plateformcommunication.Repositories.TacheRepository;
import com.ts.plateformcommunication.dto.UserDto;
import com.ts.plateformcommunication.security.user.User;
import com.ts.plateformcommunication.security.user.UserServiceImp;
import com.ts.plateformcommunication.services.ProjetService;
import com.ts.plateformcommunication.services.TacheService;
import com.ts.plateformcommunication.dto.ProjetDto;
import com.ts.plateformcommunication.dto.TacheDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class  TacheServiceImp implements TacheService {
    private final TacheRepository tacheRepository;
    private final ProjetService projetService;
    private final UserServiceImp userService;
    private final ModelMapper mapper;

   @Override
    @Transactional
    public TacheDto save(TacheDto tacheDto,Long idProjet,Long idUser) {

        ProjetDto p =projetService.findById(idProjet);
        Projet p1 =mapper.map(p,Projet.class);
        UserDto u =userService.findById(idUser);
        User u1 =mapper.map(u,User.class);
        Tache tache =mapper.map(tacheDto,Tache.class);
        tache.setProjet(p1);
        tache.setUser(u1);
        tacheRepository.save(tache);
        return mapper.map(tache,TacheDto.class);
    }




    @Override
    public TacheDto findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID ne peut pas être null");
        }

        return mapper.map(
                tacheRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("This Id Not Found")),
                TacheDto.class
        );
    }
    @Override
    public TacheDto findByName(String name) {
        return mapper.map(Optional.ofNullable(tacheRepository.findTacheByName(name))
                .orElseThrow(()->new NoSuchElementException("Tache Not Found")),TacheDto.class);
    }

    @Override
    public List<Tache> findAll() {
        return tacheRepository.findAll();
    }

    @Override
    @Transactional
    public TacheDto update( TacheDto tacheDto,Long id, Long idProjet, Long idUser) {
       Tache tache =mapper.map(tacheDto,Tache.class);
       tache.setUser(mapper.map(userService.findById(idUser),User.class));
       tache.setProjet(mapper.map(projetService.findById(idProjet),Projet.class));
       tache.setId(id);
       Tache tache1=tacheRepository.saveAndFlush(tache);
        return mapper.map(tache1, TacheDto.class);
    }


    @Override
    public String delete(Long id) {
    tacheRepository.deleteById(id);
    return "succefully,Tache is deleted";
    }

    @Override
    public List<TacheDto> findByProjetId(Long projet_id) {
        if (projet_id == null) {
            throw new IllegalArgumentException("ID de projet ne peut pas être null");
        }

        ProjetDto projetDto = projetService.findById(projet_id);

        if (projetDto == null) {
            throw new NoSuchElementException("Projet non trouvé");
        }

        Projet projet = mapper.map(projetDto, Projet.class);
        List<Tache> tacheList = tacheRepository.findByProjet(projet);

        return tacheList.stream()
                .map(tache -> mapper.map(tache, TacheDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TacheDto> findByUserId(Long user_id) {
       if(user_id==null){
           throw new IllegalArgumentException("ID de user ne peut pas être null");
       }
        UserDto userDto=userService.findById(user_id) ;
       if(userDto==null){
           throw new NoSuchElementException("User non trouvé");
       }
       User user=mapper.map(userDto,User.class);
       List<Tache> tacheList =tacheRepository.findByUser(user);
        return tacheList.stream().map(tache -> mapper.map(tache,TacheDto.class)).collect(Collectors.toList());
    }

}
