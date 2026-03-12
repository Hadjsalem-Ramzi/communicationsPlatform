package com.ts.plateformcommunication.mapper;
import com.ts.plateformcommunication.dto.UserDto;
import com.ts.plateformcommunication.security.user.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserMapper {

    private ModelMapper mapper;

    @Autowired
    public UserMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public UserDto fromUser(User user) {
        return mapper.map(user, UserDto.class);
    }

    public User fromUserDto(UserDto userDto){
        return mapper.map(userDto,User.class);
    }

}
