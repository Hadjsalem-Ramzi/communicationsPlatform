package com.ts.plateformcommunication.security.user;

import com.ts.plateformcommunication.Configuration.PageResponse;
import com.ts.plateformcommunication.dto.UserDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.ts.plateformcommunication.utils.Constants.APP_ROOT;


//@PreAuthorize("hasRole('ADMIN')")
public interface UserApi {

    @PostMapping(value = APP_ROOT+"/Users/saveUser",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    UserDto saveUser(@RequestBody UserDto User);

    @GetMapping(value = APP_ROOT +"/Users/{idUser}",produces = MediaType.APPLICATION_JSON_VALUE)
    UserDto findById(@PathVariable("idUser") Long id);

    @GetMapping(value = APP_ROOT +"/Users/{email}",produces = MediaType.APPLICATION_JSON_VALUE)
    UserDto findByEmail(@PathVariable("email") String email);

    @PutMapping(value = APP_ROOT +"/Users/update/{idUser}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    UserDto updateUser(@RequestBody UserDto User, @PathVariable("idUser") Long id);

    @DeleteMapping(value = APP_ROOT +"/Users/delete/{idUser}",produces = MediaType.APPLICATION_JSON_VALUE)
    void deleteUser(@PathVariable("idUser") Long id);

    @GetMapping(value = APP_ROOT +"/Users/all",produces = MediaType.APPLICATION_JSON_VALUE)
    PageResponse<UserDto> getUsers(@RequestParam("page") int page, @RequestParam("size") int size);


}
