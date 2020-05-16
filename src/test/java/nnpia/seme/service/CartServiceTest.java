package nnpia.seme.service;

import nnpia.seme.Creator;
import nnpia.seme.dao.CartPaggingRepository;
import nnpia.seme.dao.CartRepository;
import nnpia.seme.dto.TopUserDto;
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
    private CartItemService cartItemService;
    @Autowired
    private SeniorService seniorService;
    @Autowired
    private CartPaggingRepository paggingRepository;
    @Autowired
    private Creator creator;


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

        cartService = new CartService(cartRepository,cartItemService, seniorService, userService, paggingRepository);

        List<Cart> allWaiting = cartService.findAllDoneByUser(1);

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
        cart.setTimeDone(new Timestamp(date.getTime()));
        creator.saveEntity(cart);

        Cart responseCart = cartService.setCartToUser(cart.getId(), user.getUsername());
        Assert.assertThat(responseCart.getUser().getUsername(), is("testProfile3"));

    }

    @Test
    public void countTopUsersTest() {
        CartRepository cartRepository = Mockito.mock(CartRepository.class);
        UserService userService = Mockito.mock(UserService.class);
        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);
        User user3 = new User();
        user3.setId(3);
        User user4 = new User();
        user4.setId(4);

        Mockito.when(userService.findAll()).thenReturn(new ArrayList<User>(){{add(user1); add(user2); add(user3); add(user4);}});
        Mockito.when(cartRepository.countByUserIdAndDone(1, true)).thenReturn(10L);
        Mockito.when(cartRepository.countByUserIdAndDone(2, true)).thenReturn(5L);
        Mockito.when(cartRepository.countByUserIdAndDone(3, true)).thenReturn(9L);
        Mockito.when(cartRepository.countByUserIdAndDone(4, true)).thenReturn(1L);


        cartService = new CartService(cartRepository,cartItemService, seniorService, userService, paggingRepository);

        List<TopUserDto> list = cartService.countTopUsers();

        Assertions.assertEquals(3, list.size());
        Assertions.assertEquals(10L, list.get(2).getCount());
    }
}