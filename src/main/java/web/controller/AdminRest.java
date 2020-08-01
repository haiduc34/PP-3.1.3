package web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import web.model.UserDto;
import web.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminRest {

    private final UserService userService;

    public AdminRest(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        if ("".equals(userDto.getName()) ||
                "".equals(userDto.getPassword()) ||
                userDto.getRoles().length == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<UserDto> optionalUserDto = userService.saveUser(userDto);
        if (optionalUserDto.isPresent()) {
            return new ResponseEntity<>(optionalUserDto.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {
        Optional<UserDto> optionalUserDto = userService.updateUser(userDto);
        if (optionalUserDto.isPresent()) {
            return new ResponseEntity<>(optionalUserDto.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
