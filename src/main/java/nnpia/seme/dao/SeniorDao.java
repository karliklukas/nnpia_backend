package nnpia.seme.dao;

import nnpia.seme.model.Senior;
import nnpia.seme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeniorDao extends JpaRepository<Senior, Integer> {
    Senior findByEmail(String email);
}
