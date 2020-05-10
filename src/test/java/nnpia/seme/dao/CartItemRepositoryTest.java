package nnpia.seme.dao;

import nnpia.seme.model.Cart;
import nnpia.seme.model.CartItem;
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
        /*User user = new User();
        user.setEmail("cartUser@test.cz");
        user.setUsername("pan");
        user.setPassword("hash");
        userDao.save(user);

        Senior senior = new Senior();
        senior.setEmail("cartSen@test.cz");
        senior.setUsername("Pepa");
        senior.setCity("Praha");
        seniorDao.save(senior);

        Cart cart = new Cart();
        cart.setDone(false);
        cart.setSenior(senior);
        cart.setUser(user);
        cartDao.save(cart);*/
        Cart cart = null;
        Optional<Cart> op = cartRepository.findById(1);
        if (op.isPresent()) {
            cart = op.get();
            System.out.println(cart.getId() + " " + cart.getSenior().getEmail() + " " + cart.getUser().getEmail());
        }
        CartItem item1 = new CartItem();
        item1.setItem("prvni");
        item1.setCart(cart);
        cartItemRepository.save(item1);
        CartItem item2 = new CartItem();
        item2.setItem("druhy");
        item2.setCart(cart);
        cartItemRepository.save(item2);

        List<CartItem> all = cartItemRepository.findAll();

        for (CartItem v :all) {
            System.out.println(v.getId()+" "+v.getCart().getId()+" "+ v.getItem());
        }

        Set<CartItem> items = cart.getItems();
        for (CartItem it : items) {
            System.out.println(it.getId()+"--"+it.getCart().getId()+"--"+ it.getItem());
        }

        //System.out.println(u.getId()+" "+u.getEmail()+" "+u.getCreate_time());
        Assertions.assertEquals(2, cartItemRepository.findAll().size());

    }
}