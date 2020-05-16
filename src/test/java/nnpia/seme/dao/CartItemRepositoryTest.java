package nnpia.seme.dao;

import nnpia.seme.model.Cart;
import nnpia.seme.model.CartItem;
import nnpia.seme.model.Senior;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
public class CartItemRepositoryTest {
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SeniorRepository seniorRepository;

    @Test
    public void testAdd(){
        int initSizeItems = cartItemRepository.findAll().size();
        Senior senior = new Senior();
        senior.setEmail("cartSen@test.cz");
        senior.setUsername("Pepa");
        senior.setCity("Praha");
        seniorRepository.save(senior);

        Cart cart = new Cart();
        cart.setDone(false);
        cart.setSenior(senior);
        cart = cartRepository.saveAndFlush(cart);

        CartItem item1 = new CartItem();
        item1.setItem("prvni");
        item1.setCart(cart);
        cartItemRepository.save(item1);

        CartItem item2 = new CartItem();
        item2.setItem("druhy");
        item2.setCart(cart);
        item2 = cartItemRepository.save(item2);

        Assertions.assertEquals(initSizeItems+2, cartItemRepository.findAll().size());
        Assertions.assertEquals(cart.getId(), item2.getCart().getId());
    }
}