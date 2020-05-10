package nnpia.seme.dao;

import nnpia.seme.model.Senior;
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
public class SeniorRepositoryTest {

    @Autowired
    SeniorRepository seniorRepository;

    @Test
    public void testAdd(){
        Senior senior = new Senior();
        senior.setEmail("test@test.cz");
        senior.setUsername("Pepa");
        senior.setCity("Praha");

        seniorRepository.save(senior);

        Senior s = seniorRepository.findByEmail("test@test.cz");
        System.out.println(s.getId()+" "+s.getEmail()+" "+s.getUsername()+" "+s.getCity()+" "+s.getCreate_time());
        Assertions.assertEquals(1, seniorRepository.findAll().size());
    }
}