package nnpia.seme.dao;

import nnpia.seme.model.Senior;
import nnpia.seme.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
public class SeniorDaoTest {

    @Autowired
    SeniorDao seniorDao;

    @Test
    public void testAdd(){
        Senior senior = new Senior();
        senior.setEmail("test@test.cz");
        senior.setUsername("Pepa");
        senior.setCity("Praha");

        seniorDao.save(senior);

        Senior s = seniorDao.findByEmail("test@test.cz");
        System.out.println(s.getId()+" "+s.getEmail()+" "+s.getUsername()+" "+s.getCity()+" "+s.getCreate_time());
        Assertions.assertEquals(1, seniorDao.findAll().size());
    }
}