package nnpia.seme.dao;

import nnpia.seme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    User findByUsername(String username);
}
