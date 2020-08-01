package web.dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import web.model.Role;
import web.model.User;
import web.model.UserDto;

import java.util.List;

public interface UserDao  {
    User getUserByName(String name);

    List<UserDto> getUsers();

    boolean saveUser(User user);

    boolean updateUser(User user);

    void deleteUser(long id);

}

