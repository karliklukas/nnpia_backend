package nnpia.seme.dao;

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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
//@Rollback(false)
public class CartDaoTest {
    @Autowired
    CartDao cartDao;

    @Autowired
    UserDao userDao;

    @Autowired
    SeniorDao seniorDao;


    @Test
    public void testAdd(){

        User user = new User();
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
       // cart.setUser(user);

        cartDao.save(cart);
        List<Cart> all = cartDao.findAll();

        for (Cart v :all) {
            if (v.getUser() != null){
                System.out.println(v.getId()+" "+v.getSenior().getEmail()+" "+ v.getUser().getEmail());
            }else {
                System.out.println(v.getId()+" "+v.getSenior().getEmail()+" null");
            }

        }

        // Senior s = cartDao.findByEmail("test@test.cz");
        //System.out.println(s.getId()+" "+s.getEmail()+" "+s.getUsername()+" "+s.getCity()+" "+s.getCreate_time());
        Assertions.assertEquals(2, cartDao.findAll().size());
    }

   /* @Test
    public void testAddMock(){
        when(dao.getEmployeeById(1)).thenReturn(new EmployeeVO(1,"Lokesh","Gupta","user@email.com"));

        EmployeeVO emp = manager.getEmployeeById(1);

        assertEquals("Lokesh", emp.getFirstName());
        assertEquals("Gupta", emp.getLastName());
        assertEquals("user@email.com", emp.getEmail());
    }*/
}