package com.ts.plateformcommunication.security.user;

import com.ts.plateformcommunication.Configuration.PageResponse;
import com.ts.plateformcommunication.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.ts.plateformcommunication.utils.Constants.APP_ROOT;

@RestController
@PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController implements UserApi {

    private final UserServiceImp service;

    @PatchMapping(APP_ROOT+"/updatePassword")
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }



    private  final UserServiceImp  userServiceImp;


    @Override
    public UserDto saveUser(UserDto User) {
        return userServiceImp.saveUser(User);
    }

    @Override
    public UserDto findById(Long id) {
        return userServiceImp.findById(id);
    }

    @Override
    public UserDto findByEmail(String email) {
        return userServiceImp.findByEmail(email);
    }

    @Override
    public UserDto updateUser(UserDto User, Long id) {
        return userServiceImp.updateUser(User, id);
    }

    @Override
    public void deleteUser(Long id) {
        userServiceImp.deleteUser(id);
    }

    @Override
    public PageResponse<UserDto> getUsers(int page, int size) {
        return userServiceImp.getUsers(page, size);
    }

}
