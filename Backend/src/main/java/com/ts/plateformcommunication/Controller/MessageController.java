package com.ts.plateformcommunication.Controller;
import com.ts.plateformcommunication.services.MessageService;
import com.ts.plateformcommunication.dto.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Message")
public class MessageController {
    private MessageService messageService ;
    @Autowired
    public MessageController(MessageService messageService){
         this.messageService=messageService;
    }
    @GetMapping("/findById/{id}")
    public MessageDto findById(@PathVariable("id") Long id){
        return messageService.findById(id);
    }

    @GetMapping("/findByName/{name}")
    public MessageDto findByName(@PathVariable("name") String name) {
        return messageService.findByName(name);
    }

    @GetMapping("/findAll")
    public List<MessageDto> findAll() {
        return messageService.findAll();
    }

    @PostMapping("/save/{idChat}/{idr}")
    public MessageDto save( @RequestBody MessageDto messageDto,@PathVariable("idChat") Long idChat,@PathVariable("idr") Long idr){
        return  messageService.save(messageDto,idChat,idr);
    }

    @PutMapping("/update/{messageId}")
    public ResponseEntity<MessageDto> update(@PathVariable("messageId") Long messageId, @RequestBody MessageDto updatedMessageDto) {
        try {
            // Appeler la méthode de mise à jour dans le service
            MessageDto updatedMessage = messageService.update(updatedMessageDto, messageId);
            return new ResponseEntity<>(updatedMessage, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Retourner une réponse avec le statut BAD_REQUEST en cas d'erreur
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete (@PathVariable("id") Long id){
      String accessMessage=messageService.delete(id);
      return new ResponseEntity<>(accessMessage, HttpStatus.OK);
    }

    @GetMapping("/messages/{chatRoomId}")
    public List<MessageDto> getAllMessagesByChatRoom(@PathVariable("chatRoomId") Long chatRoomId) {
        return messageService.findAllByChatRoomId(chatRoomId);
    }

}
