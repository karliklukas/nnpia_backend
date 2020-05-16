package nnpia.seme.dao;

import nnpia.seme.model.User;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;


@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void testAdd(){
        User user = new User();
        user.setEmail("testUserAdd@test.cz");
        user.setUsername("pan");
        user.setPassword("hash");

        User save = userRepository.save(user);

        User u = userRepository.findByEmail("testUserAdd@test.cz");

        Assertions.assertEquals("pan", u.getUsername());

    }
}