package web.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.UserDao;
import web.model.User;
import web.model.UserDto;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, RoleService roleService, PasswordEncoder passwordEncoder, PasswordEncoder passwordEncoder1) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder1;
    }

    @Override
    @Transactional(readOnly=true)
    public List<UserDto> getUsers() {
        return userDao.getUsers();
    }

    @Override
    public Optional<UserDto> saveUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = new User(userDto);
        user.setRoles(Arrays.stream(userDto.getRoles()).map(roleService::getRoleByName).collect(Collectors.toSet()));
        if (userDao.saveUser(user)) {
            user = (User) userDao.getUserByName(user.getUsername());
            userDto.setId(user.getId());
            userDto.setPassword(user.getPassword());
            userDto.setPassword("ENCRYPTED");
            return Optional.of(userDto);
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> updateUser(UserDto userDto) {
        if (!userDto.getPassword().isEmpty()) {
            String hashPassword = passwordEncoder.encode(userDto.getPassword());
            userDto.setPassword(hashPassword);
        } else {
            User userFromDB = (User) userDao.getUserByName(userDto.getName());
            userDto.setPassword(userFromDB.getPassword());
        }
        User user = new User(userDto);
        user.setRoles(Arrays.stream(userDto.getRoles()).map(roleService::getRoleByName).collect(Collectors.toSet()));
        if (userDao.updateUser(user)) {
            userDto.setPassword("ENCRYPTED");
            return Optional.of(userDto);
        }
        return Optional.empty();
    }

    @Override
    public void deleteUser(long id) {
        userDao.deleteUser(id);
    }

}
