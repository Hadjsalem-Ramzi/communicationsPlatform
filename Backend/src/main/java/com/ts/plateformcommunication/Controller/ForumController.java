package com.ts.plateformcommunication.Controller;
import com.ts.plateformcommunication.services.Impl.ForumServiceImp;
import com.ts.plateformcommunication.dto.ForumDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Forum")
public class ForumController {
    private ForumServiceImp  forumServiceImp;
    @Autowired
    public ForumController(ForumServiceImp forumServiceImp){
        this.forumServiceImp=forumServiceImp;
    }
   @GetMapping("/findById/{id}")
   public ForumDto findById(@PathVariable("id") Long id){
        return forumServiceImp.findById(id);
   }

    @PostMapping("/save")
    public ForumDto save(@RequestBody ForumDto forumDto){
        return  forumServiceImp.save(forumDto);
    }

    @GetMapping("/findByName/{name}")
    public ForumDto findByName(@PathVariable("name") String name) {
        return forumServiceImp.findByName(name);
    }

    @GetMapping("/findAll")
    public List<ForumDto> findAll() {
        return forumServiceImp.findAll();
    }

    @PutMapping("/update/{id}")
    public ForumDto update(@PathVariable("id") Long id, @RequestBody ForumDto forumDto) {
        ForumDto Em = forumServiceImp.findById(id);
        if (Em != null) {
            forumDto.setId(id);
            return forumServiceImp.update(forumDto);
        } else {
            throw new RuntimeException("Failed Modification");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete (@PathVariable("id") Long id){
        String successMessage =forumServiceImp.delete(id);
        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }
}









