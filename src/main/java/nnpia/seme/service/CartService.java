package nnpia.seme.service;

import nnpia.seme.dao.CartDao;
import nnpia.seme.dao.CartItemDao;
import nnpia.seme.model.Cart;
import nnpia.seme.model.CartItem;
import nnpia.seme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class CartService {
    private List<String> itemList = new ArrayList<>();

    //@Autowired
    private CartDao cartDao;

    //@Autowired
    private CartItemService cartItemService;

    //@Autowired
    private SeniorService seniorService;

    //@Autowired
    private UserService userService;

    @Autowired
    public CartService(CartDao cartDao, CartItemService cartItemService, SeniorService seniorService, UserService userService) {
        this.cartDao = cartDao;
        this.cartItemService = cartItemService;
        this.seniorService = seniorService;
        this.userService = userService;
    }

    public List<Cart> findAll() {
        return cartDao.findAll();
    }

    public List<Cart> findAllFreeCards() {
        return cartDao.findAll().stream().filter(cart -> !cart.isDone() && cart.getUser()==null).collect(Collectors.toList());
    }

    public List<Cart> findAllDoneByUser(Integer id) {
        User user = userService.findById(id);
        return cartDao.findAll().stream().filter(cart -> cart.isDone() && cart.getUser().equals(user)).collect(Collectors.toList());
    }

    public List<Cart> findAllWaitingByUser(Integer id) {
        User user = userService.findById(id);
        return cartDao.findAll().stream().filter(cart -> !cart.isDone() && cart.getUser().equals(user)).collect(Collectors.toList());
    }

    public int countDoneByUser(Integer id){
        return findAllDoneByUser(id).size();
    }

    public int countWitingByUser(Integer id){
        return findAllWaitingByUser(id).size();
    }

    public Cart findById(Integer id) {
        if (cartDao.findById(id).isPresent()) {
            return cartDao.findById(id).get();
        } else {
            throw new NoSuchElementException("Product with ID: " + id + " was not found!");
        }
    }

    public List<CartItem> getAllItems(Integer id){
        Cart cart = findById(id);
        return new ArrayList<>(cart.getItems());
    }

    public void setCartDone(Integer id){
        Cart cart = findById(id);
        cart.setDone(true);
        cartDao.save(cart);
    }

    public void addItemToCart(String item) {
        itemList.add(item);
    }

    public void completeOrder(Integer id) {
        Cart cart = new Cart();
        cart.setDone(false);
        cart.setSenior(seniorService.findById(id));
        cartDao.save(cart);

        for (String item: itemList) {
            cartItemService.addCartItem(item, cart);
        }

        itemList.clear();
    }

}
