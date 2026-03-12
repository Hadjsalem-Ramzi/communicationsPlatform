package com.ts.plateformcommunication.Controller;
import com.ts.plateformcommunication.services.ProjetService;
import com.ts.plateformcommunication.dto.ProjetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Project")
public class ProjetController {
    private ProjetService projetService;
  @Autowired
    public ProjetController(ProjetService projetService){
        this.projetService=projetService;
    }

    @GetMapping("/findById/{id}")
    public ProjetDto findById(@PathVariable("id") Long id){
        return projetService.findById(id);
    }

    @GetMapping("/findByName/{name}")
    public ProjetDto findByName(@PathVariable("name") String name) {
        return projetService.findByName(name);
    }

    @GetMapping("/findAll")
    public List<ProjetDto> findAll() {
        return projetService.findAll();
    }

    @PostMapping("/save")
    public ProjetDto save(@RequestBody ProjetDto projetDto){
        return projetService.save(projetDto);
    }

    @PutMapping("/update/{id}")
    public ProjetDto update(@PathVariable("id") Long id, @RequestBody ProjetDto projetDto) {
        ProjetDto Em = projetService.findById(id);
        if (Em != null) {
            projetDto.setId(id);
            return projetService.update(projetDto);
        } else {
            throw new RuntimeException("Failed Modification");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete (@PathVariable("id") Long id){
       String successMessage = projetService.delete(id);
        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }



}
