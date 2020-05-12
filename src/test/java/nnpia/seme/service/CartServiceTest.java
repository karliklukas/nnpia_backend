package nnpia.seme.service;

import nnpia.seme.Creator;
import nnpia.seme.dao.CartPaggingRepository;
import nnpia.seme.dao.CartRepository;
import nnpia.seme.model.Cart;
import nnpia.seme.model.Senior;
import nnpia.seme.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.is;

@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@SpringBootTest
public class CartServiceTest {

    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private SeniorService seniorService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartPaggingRepository paggingRepository;
    @Autowired
    private Creator creator;

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
    public void findAllDoneMock() {
        CartRepository cartRepository = Mockito.mock(CartRepository.class);
        UserService userService = Mockito.mock(UserService.class);
        User user = new User();
        user.setId(1);

        Cart cart = new Cart();
        cart.setId(1);
        cart.setDone(true);
        cart.setUser(user);

        Cart cart1 = new Cart();
        cart1.setId(2);
        cart1.setDone(false);
        cart1.setUser(user);

        Mockito.when(cartRepository.findAll()).thenReturn(new ArrayList<Cart>(){{add(cart); add(cart1);}});
        Mockito.when(userService.findById(1)).thenReturn(user);

        System.out.println(cartRepository.findAll().get(0).getId());

        cartService = new CartService(cartRepository,cartItemService, seniorService, userService, paggingRepository);

        List<Cart> allWaiting = cartService.findAllDoneByUser(1);
        System.out.println(allWaiting.get(0).getId()+"--"+allWaiting.get(0).isDone());
        Assertions.assertEquals(1, allWaiting.size());
    }

    @Test
    public void setCartToUserTest(){
        Date date= new Date();
        User userFalse = new User();
        userFalse.setCreate_time(new Timestamp(date.getTime()));//Creater neumi vytvorit timestamp

        User user = new User();
        user.setUsername("testProfile3");
        user.setCreate_time(new Timestamp(date.getTime()));//Creater neumi vytvorit timestamp
        creator.saveEntity(user);

        Senior sen = new Senior();
        sen.setCreate_time(new Timestamp(date.getTime()));//Creater neumi vytvorit timestamp

        Cart cart = new Cart();
        cart.setSenior(sen);
        cart.setUser(userFalse);
        cart.setItems(new HashSet<>());//Creater neumi vytvorit set
        cart.setTime(new Timestamp(date.getTime()));
        creator.saveEntity(cart);

        Cart responseCart = cartService.setCartToUser(cart.getId(), user.getUsername());
        Assert.assertThat(responseCart.getUser().getUsername(), is("testProfile3"));

    }
}