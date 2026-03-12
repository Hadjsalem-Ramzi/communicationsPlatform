package com.ts.plateformcommunication.Controller;
import com.ts.plateformcommunication.Model.Tache;
import com.ts.plateformcommunication.services.Impl.TacheServiceImp;
import com.ts.plateformcommunication.dto.TacheDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/Tache")
@CrossOrigin(origins = "http://localhost:4200" )
public class TacheController {
    @Autowired
    private TacheServiceImp tacheServiceImp;
    public TacheController(TacheServiceImp tacheServiceImp){
        this.tacheServiceImp=tacheServiceImp;
    }

    @GetMapping("/findById/{id}")
    public TacheDto findById(@PathVariable("id") Long id){
        return tacheServiceImp.findById(id);
    }

    @GetMapping("/findByName/{name}")
    public TacheDto findByName(@PathVariable("name") String name) {
        return tacheServiceImp.findByName(name);
    }

    @GetMapping("/findAll")
    public List<Tache> findAll() {
        return tacheServiceImp.findAll();
    }

   @PostMapping("/save/{idP}/{idU}")
    public TacheDto save(@RequestBody TacheDto tacheDto,@PathVariable("idP") Long idP,@PathVariable("idU") Long idU){
        return tacheServiceImp.save(tacheDto,idP,idU);

    }


    @PutMapping("/update/{idT}/{idProjet}/{idUser}")
    public TacheDto update(@PathVariable("idT") Long idT, @RequestBody TacheDto tacheDto, @PathVariable("idProjet") Long idProjet, @PathVariable("idUser") Long idUser) {
     TacheDto T1 = tacheServiceImp.findById(idT);
      if(T1!=null){
          tacheDto.setId(idT);
          return tacheServiceImp.update(tacheDto,idT,idProjet,idUser);
      }else {
          throw  new RuntimeException("Failed Modification");
      }

    }




    @GetMapping("/findByProjetId/{projet_id}")
    public List<TacheDto> findByProjetId(@PathVariable("projet_id") Long projet_id) {
        return tacheServiceImp.findByProjetId(projet_id);
    }

    @GetMapping("/findByUserId/{user_id}")
    public List<TacheDto> findByUserId(@PathVariable("user_id") Long user_id) {
        return tacheServiceImp.findByUserId(user_id);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete (@PathVariable("id") Long id){
        String accessMessage=tacheServiceImp.delete(id);
        return new ResponseEntity<>(accessMessage, HttpStatus.OK);
    }

}
