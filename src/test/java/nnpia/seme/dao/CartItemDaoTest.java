package nnpia.seme.dao;

import nnpia.seme.model.Cart;
import nnpia.seme.model.CartItem;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
public class CartItemDaoTest {
    @Autowired
    CartItemDao cartItemDao;
    @Autowired
    CartDao cartDao;
    @Autowired
    UserDao userDao;
    @Autowired
    SeniorDao seniorDao;

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
        Optional<Cart> op = cartDao.findById(1);
        if (op.isPresent()) {
            cart = op.get();
            System.out.println(cart.getId() + " " + cart.getSenior().getEmail() + " " + cart.getUser().getEmail());
        }
        CartItem item1 = new CartItem();
        item1.setItem("prvni");
        item1.setCart(cart);
        cartItemDao.save(item1);
        CartItem item2 = new CartItem();
        item2.setItem("druhy");
        item2.setCart(cart);
        cartItemDao.save(item2);

        List<CartItem> all = cartItemDao.findAll();

        for (CartItem v :all) {
            System.out.println(v.getId()+" "+v.getCart().getId()+" "+ v.getItem());
        }

        Set<CartItem> items = cart.getItems();
        for (CartItem it : items) {
            System.out.println(it.getId()+"--"+it.getCart().getId()+"--"+ it.getItem());
        }

        //System.out.println(u.getId()+" "+u.getEmail()+" "+u.getCreate_time());
        Assertions.assertEquals(2, cartItemDao.findAll().size());

    }
}