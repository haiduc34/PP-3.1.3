package web.dao;

import web.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public interface RoleDao {

    Role getRoleByName(String name);

    void addRole(Role role);

    Role getRoleById(Long id);

}
