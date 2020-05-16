package nnpia.seme.dao;

import nnpia.seme.model.Cart;
import nnpia.seme.model.Senior;
import nnpia.seme.model.User;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
//@Rollback(false)
public class CartRepositoryTest {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SeniorRepository seniorRepository;


    @Test
    public void testAdd(){
        int initSize =  cartRepository.findAll().size();
        User user = new User();
        user.setEmail("cartUser@test.cz");
        user.setUsername("pan");
        user.setPassword("hash");
        userRepository.save(user);

        Senior senior = new Senior();
        senior.setEmail("cartSen@test.cz");
        senior.setUsername("Pepa");
        senior.setCity("Praha");
        seniorRepository.save(senior);

        Cart cart = new Cart();
        cart.setDone(false);
        cart.setSenior(senior);
        cart.setUser(user);

        cartRepository.save(cart);

        Assertions.assertEquals(initSize+1, cartRepository.findAll().size());
    }

}