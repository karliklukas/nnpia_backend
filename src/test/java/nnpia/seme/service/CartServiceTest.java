package nnpia.seme.service;

import nnpia.seme.dao.CartDao;
import nnpia.seme.model.Cart;
import nnpia.seme.model.CartItem;
import nnpia.seme.model.Senior;
import nnpia.seme.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@SpringBootTest
public class CartServiceTest {

    @Autowired
    private CartService cartService;
    @Autowired
    private CartDao cartDao;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private SeniorService seniorService;

    @Autowired
    private UserService userService;

    @Test
    public void findAllWaiting() {
        List<Cart> allWaiting = cartService.findAllWaitingByUser(1);
        System.out.println(allWaiting.get(0).getId());
        Assertions.assertEquals(1, allWaiting.size());
    }

    @Test
    public void findAllWaitingByUser() {
        List<Cart> allWaiting = cartService.findAllWaitingByUser(11);
        System.out.println(allWaiting.get(0).getId()+" "+allWaiting.get(0).getUser().getUsername());
        Assertions.assertEquals(1, allWaiting.size());
    }

    @Test
    public void findAllDone() {
        CartDao cartDao = Mockito.mock(CartDao.class);
        Cart cart = new Cart();
        cart.setId(1);
        cart.setDone(true);

        Mockito.when(cartDao.findAll()).thenReturn(new ArrayList<Cart>(){{add(cart);}});
        System.out.println(cartDao.findAll().get(0).getId());

        cartService = new CartService(cartDao, cartItemService, seniorService, userService);

        List<Cart> allWaiting = cartService.findAllDoneByUser(1);
        System.out.println(allWaiting.get(0).getId()+"--"+allWaiting.get(0).isDone());
        Assertions.assertEquals(1, allWaiting.size());
    }
}