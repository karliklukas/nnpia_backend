package nnpia.seme.service;

import nnpia.seme.dao.CartItemDao;
import nnpia.seme.model.Cart;
import nnpia.seme.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CartItemService {
    @Autowired
    private CartItemDao cartItemDao;

    public List<CartItem> findAll() {
        return cartItemDao.findAll();
    }

    public CartItem findById(Integer id) {
        if (cartItemDao.findById(id).isPresent()) {
            return cartItemDao.findById(id).get();
        } else {
            throw new NoSuchElementException("Product with ID: " + id + " was not found!");
        }
    }

    public void addCartItem(String text, Cart cart){
        CartItem cartItem = new CartItem();
        cartItem.setItem(text);
        cartItem.setCart(cart);
        cartItemDao.save(cartItem);
    }

}
