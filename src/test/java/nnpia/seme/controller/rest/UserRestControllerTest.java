package nnpia.seme.controller.rest;

import nnpia.seme.Creator;
import nnpia.seme.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.sql.Timestamp;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRestControllerTest {

    @Autowired
    private UserRestController userController;

    @Autowired
    private Creator creator;

    @Test
    public void getUserByEmail() {
        Date date= new Date();
        User user = new User();
        user.setUsername("testProfile1");
        user.setEmail("testProfile1@prg.com");
        user.setCreate_time(new Timestamp(date.getTime()));
        creator.saveEntity(user);

        User responseUser = userController.getUserByEmail(user.getEmail());
        Assert.assertThat(responseUser.getUsername(), is("testProfile1"));
    }
}