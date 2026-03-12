package com.ts.plateformcommunication.Controller;
import com.ts.plateformcommunication.services.Impl.FichierServiceImp;
import com.ts.plateformcommunication.dto.FichierDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Fichier")
public class FichierController {
    private FichierServiceImp fichierServiceImp;
   @Autowired
    public FichierController(FichierServiceImp fichierServiceImp) {
        this.fichierServiceImp = fichierServiceImp;
    }

    @PostMapping("/save/{idF}")
    public FichierDto save(@RequestBody FichierDto fichierDto,@PathVariable("idF") Long idF){
        return  fichierServiceImp.save(fichierDto,idF);
    }

    @GetMapping("/findById/{id}")
    public FichierDto findById(@PathVariable("id") Long id){
         return  fichierServiceImp.findById(id);
    }

    @GetMapping("/findByName/{name}")
    public FichierDto findByName(@PathVariable("name") String name){
        return  fichierServiceImp.findByName(name);
    }
    @GetMapping("/findAll")
    public List<FichierDto> findAll(){
        return  fichierServiceImp.findAll();
    }

    @PutMapping("/update/{idF}/{idU}")
    public FichierDto update(@PathVariable("idF") Long idF,@RequestBody FichierDto fichierDto,@PathVariable("idU") Long idU){
       FichierDto F1 = fichierServiceImp.findById(idF);
       if(F1!=null){
           fichierDto.setId(idF);
           return fichierServiceImp.update(fichierDto,idF,idU);
       }else {
           throw  new RuntimeException("Failed Modification");
       }
    }

    @GetMapping("/findByUserId/{user_id}")
    public List<FichierDto> findByUsersId(@PathVariable("user_id") Long user_id) {
        return fichierServiceImp.findByUserId(user_id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
     String  successMessage=fichierServiceImp.delete(id);
       return new  ResponseEntity<>(successMessage,HttpStatus.OK);
    }

    @GetMapping("/findAllFiles/{idU}")
    public List<FichierDto>  findAllFiles(@PathVariable("idU") Long idU){
       return  fichierServiceImp.findAllFiles(idU);
    }

  @GetMapping("/findByOrdreName")
    public List<FichierDto> findByOrdreName(){
       return fichierServiceImp.findAllOrderedByName();
  }

}
