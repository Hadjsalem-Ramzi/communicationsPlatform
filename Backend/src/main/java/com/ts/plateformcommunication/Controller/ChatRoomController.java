package com.ts.plateformcommunication.Controller;
import com.ts.plateformcommunication.services.Impl.ChatroomServiceImp;
import com.ts.plateformcommunication.dto.ChatroomDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@RequestMapping("/ChatRoom")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatRoomController {

    private ChatroomServiceImp   chatroomServiceImp;
  @Autowired
    public ChatRoomController(ChatroomServiceImp serviceImp) {
        this.chatroomServiceImp = serviceImp;
    }

    @PostMapping("/save/{idForum}")
    public ChatroomDto save(@RequestBody ChatroomDto chatroomDto,@PathVariable("idForum") Long idF){
      return chatroomServiceImp.save(chatroomDto,idF);
    }

    @GetMapping("/findById/{id}")
    public ChatroomDto findById(@PathVariable("id") Long id){
      return chatroomServiceImp.findById(id);
    }

    @GetMapping("/findByName/{name}")
    public ChatroomDto findByName(@PathVariable("name") String name){
      return chatroomServiceImp.findByName(name);
    }

    @GetMapping("/findAll")
    public List<ChatroomDto> findAll(){
      return chatroomServiceImp.findAll();
    }

    @PutMapping("/update/{id}/{idForum}")
    public ChatroomDto update(@PathVariable("id") Long id,@RequestBody ChatroomDto chatroomDto,@PathVariable("idForum") Long idF){
      ChatroomDto chat=chatroomServiceImp.findById(id);
      if(chat!=null){
          chatroomDto.setId(id);
          return chatroomServiceImp.update(chatroomDto,idF);
      } else {
          throw  new RuntimeException("Failed Modification");
      }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
      String successMessage=chatroomServiceImp.delete(id);
      return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }

    @GetMapping("/findByForumId/{idForum}")
   List<ChatroomDto>findByForumId(@PathVariable("idForum") Long idForum){
    return chatroomServiceImp.findByForumId(idForum);
    }

}
