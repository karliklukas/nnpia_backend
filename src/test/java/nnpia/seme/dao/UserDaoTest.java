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
public class UserDaoTest {

    @Autowired
    UserDao userDao;

    @Test
    public void testAdd(){
        User user = new User();
        String test = "test";
        user.setEmail("test2@test.cz");
        user.setUsername("pan");
        user.setPassword("hash");

        User save = userDao.save(user);
        System.out.println(save.getId()+"++");

        User u = userDao.findByEmail("test2@test.cz");
        System.out.println(u.getId()+" "+u.getEmail()+" "+u.getCreate_time());
        Assertions.assertEquals(4, userDao.findAll().size());

    }
}