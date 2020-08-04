package web.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import web.model.UserDto;
import web.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminRest {

    private final UserService userService;
    private final UserDetailsService userDetailsService;

    public AdminRest(UserService userService, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {

        if ("".equals(userDto.getName()) ||
                "".equals(userDto.getPassword()) ||
                userDto.getRoles().length == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<UserDto> optionalUserDto = userService.saveUser(userDto);
        if (optionalUserDto.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(optionalUserDto.get());
            //return new ResponseEntity<>(optionalUserDto.get(), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {
        Optional<UserDto> optionalUserDto = userService.updateUser(userDto);
        if (optionalUserDto.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(optionalUserDto.get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
