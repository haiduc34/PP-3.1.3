package web.dao;

import org.springframework.stereotype.Repository;
import web.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role getRoleByName(String name) {

        Query query = entityManager.createQuery("from Role where role = :role")
                .setParameter("role", name)
                .setMaxResults(1);


        Role role = (Role) query.getSingleResult();

        return role;
    }

    @Override
    public Role getRoleById(Long id) {
        Query query = entityManager.createQuery("from Role where id = :id");
        Role role = (Role) query.getSingleResult();
        return role;
    }

    @Override
    public void addRole(Role role) {
        entityManager.persist(role);
    }
}
