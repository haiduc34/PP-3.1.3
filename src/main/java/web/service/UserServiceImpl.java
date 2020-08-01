package web.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.UserDao;
import web.model.Role;
import web.model.User;
import web.model.UserDto;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final RoleService roleService;

    public UserServiceImpl(UserDao userDao, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleService = roleService;
    }

    @Override
    @Transactional(readOnly=true)
    public List<UserDto> getUsers() {
        return userDao.getUsers();
    }

    @Override
    public Optional<UserDto> saveUser(UserDto userDto) {
        User user = new User(userDto);
        user.setRoles(Arrays.stream(userDto.getRoles()).map(roleService::getRoleByName).collect(Collectors.toSet()));
        if (userDao.saveUser(user)) {
            user = (User) userDao.getUserByName(user.getUsername());
            userDto.setId(user.getId());
            // userDto.setPassword("");
            return Optional.of(userDto);
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> updateUser(UserDto userDto) {
        if (userDto.getPassword().isEmpty()) {
            User userFromDB = (User) userDao.getUserByName(userDto.getName());
            userDto.setPassword(userFromDB.getPassword());
        }

        User user = new User(userDto);
        user.setRoles(Arrays.stream(userDto.getRoles()).map(roleService::getRoleByName).collect(Collectors.toSet()));

        if (userDao.updateUser(user)) {
            return Optional.of(userDto);
        }

        return Optional.empty();
    }

    @Override
    public void deleteUser(long id) {
        userDao.deleteUser(id);
    }

}
