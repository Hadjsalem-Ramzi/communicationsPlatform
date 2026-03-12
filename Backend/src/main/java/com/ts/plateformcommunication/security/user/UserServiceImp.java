package com.ts.plateformcommunication.security.user;
import com.ts.plateformcommunication.Configuration.PageResponse;
import com.ts.plateformcommunication.dto.UserDto;
import com.ts.plateformcommunication.exception.DuplicateEntryException;
import com.ts.plateformcommunication.mapper.UserMapper;
import com.ts.plateformcommunication.validators.ObjectsValidators;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service

public class UserServiceImp implements UserService {

    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ObjectsValidators<User> userValidators;

    @Autowired
    public UserServiceImp(UserMapper mapper, PasswordEncoder passwordEncoder, UserRepository userRepository, ObjectsValidators<User> userValidators) {
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userValidators = userValidators;
    }


    public   void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        userRepository.save(user);
    }


    @Override
    public UserDto findById(Long id) {
        Optional<User> optionalUser =userRepository.findById(id);
        return optionalUser.map(mapper::fromUser).orElseThrow(()->new EntityNotFoundException("User Not Found"));

    }

    @Override
    public UserDto findByEmail(String email) {
        Optional<User> User = userRepository.findByEmail(email);
        if(User.isEmpty()){
            throw  new EntityNotFoundException("User not Found");
        }
        return mapper.fromUser(User.get());
    }

    public UserDto findUserByFirstName(String email) {
        Optional<User> User = userRepository.findByEmail(email);
        if (User.isEmpty()) {
            throw new RuntimeException("User Not Found");
        }
        return mapper.fromUser(User.get());
    }

    @Override
    public UserDto saveUser(UserDto UserDto) {
        User User = mapper.fromUserDto(UserDto);
        if(User == null){
            throw new IllegalArgumentException("User est null");
        }

        //validation des champs
        userValidators.validate(User);
        boolean exists = userRepository.existsByEmail(UserDto.getEmail());
        if(exists){
            throw new DuplicateEntryException("un User est existe avec cette email");
        }
        User savedUser = userRepository.save(User);
        return mapper.fromUser(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto Userdto, Long id) {
        Optional<User> User1 = userRepository.findById(id);
        if(User1.isPresent()){
            User user2 = User1.get();
            user2.setId(Long.valueOf(id));
            userValidators.validate(user2);
            if (userRepository.existsByEmail(user2.getEmail())){
                throw  new DuplicateEntryException(" un User est existe avec cette email");
            }
            User User3= userRepository.saveAndFlush(user2);
            return mapper.fromUser(User3);
        }else {
            throw  new EntityNotFoundException("User NotFound");
        }
    }

    @Override
    public void deleteUser(Long id) {
        if(userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("User with this id Not Found");
        }

    }

    public PageResponse<UserDto> getUsers(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<User> Users = userRepository.findAll(pageRequest);
        List<UserDto> UserList = Users.map(mapper::fromUser).getContent();


        return new PageResponse<>(
                UserList,
                Users.getNumber(),
                Users.getSize(),
                Users.getTotalElements(),
                Users.getTotalPages(),
                Users.isFirst(),
                Users.isLast()
        );
    }



}

    
    
    

