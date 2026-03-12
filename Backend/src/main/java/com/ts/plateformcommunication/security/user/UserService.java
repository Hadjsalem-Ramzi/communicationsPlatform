package com.ts.plateformcommunication.security.user;

import com.ts.plateformcommunication.Configuration.PageResponse;
import com.ts.plateformcommunication.dto.UserDto;

public interface UserService {
    UserDto findById(Long id);
    UserDto findByEmail(String email);
    UserDto saveUser( UserDto User);
    UserDto updateUser( UserDto User,Long id);
    void deleteUser(Long id);

  PageResponse<UserDto> getUsers(int page , int size);


}
