package nnpia.seme.service;

import nnpia.seme.dao.CartRepository;
import nnpia.seme.dao.CartPaggingRepository;
import nnpia.seme.dto.CartPagingDto;
import nnpia.seme.dto.TopUserDto;
import nnpia.seme.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class CartService {
    private final List<String> itemList = new ArrayList<>();

    private final CartRepository cartRepository;
    private final CartPaggingRepository cartPaging;
    private final CartItemService cartItemService;
    private final SeniorService seniorService;
    private final UserService userService;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemService cartItemService, SeniorService seniorService, UserService userService, CartPaggingRepository cartPaging) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.seniorService = seniorService;
        this.userService = userService;
        this.cartPaging = cartPaging;
    }

    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    public List<Cart> findAllDoneByUser(Integer id) {
        User user = userService.findById(id);
        return cartRepository.findAll().stream().filter(cart -> cart.isDone() && cart.getUser().equals(user)).collect(Collectors.toList());
    }

    public List<Cart> findAllWaitingByUser(Integer id) {
        User user = userService.findById(id);
        return cartRepository.findAll().stream().filter(cart -> !cart.isDone() && cart.getUser().equals(user)).collect(Collectors.toList());
    }

    public Long countFreeCarts() {
        return cartRepository.countByUserIdAndDone(null, false);
    }

    public Long countDoneByUser(Integer id) {
        return cartRepository.countByUserIdAndDone(id, true);
    }

    public Long countWaitingByUser(Integer id) {
        return cartRepository.countByUserIdAndDone(id, false);
    }

    public Cart findById(Integer id) {
        if (cartRepository.findById(id).isPresent()) {
            return cartRepository.findById(id).get();
        } else {
            throw new NoSuchElementException("Order with ID: " + id + " was not found!");
        }
    }

    public List<CartItem> getAllItems(Integer id) {
        Cart cart = findById(id);
        return new ArrayList<>(cart.getItems());
    }

    public void setCartDone(Integer id) {
        Date date= new Date();
        Cart cart = findById(id);
        cart.setDone(true);
        cart.setTimeDone(new Timestamp(date.getTime()));
        cartRepository.save(cart);
    }

    public Cart setCartToUser(Integer idCart, String username) {
        User user = userService.findOne(username);
        Cart cart = cartRepository.getOne(idCart);
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    public void addItemToCart(String item) {
        itemList.add(item);
    }

    public Integer completeOrder(Integer id) {
        Cart cart = new Cart();
        cart.setDone(false);
        cart.setSenior(seniorService.findById(id));
        cart = cartRepository.save(cart);

        for (String item : itemList) {
            cartItemService.addCartItem(item, cart);
        }

        itemList.clear();
        return cart.getId();
    }

    public CartPagingDto getAllFreeCartsPaSo(Pageable paging, Integer userId, Boolean done) {
        Page<Cart> pagedResult;
        if (userId == -1) {
            pagedResult = cartPaging.findAllByUserIdAndDone(null, done, paging);
        } else {
            pagedResult = cartPaging.findAllByUserIdAndDone(userId, done, paging);
        }

        if (pagedResult.hasContent()) {
            CartPagingDto cart = new CartPagingDto();
            cart.setList(pagedResult.getContent());
            cart.setTotalElements(pagedResult.getTotalElements());
            return cart;
        } else {
            return new CartPagingDto();
        }
    }

    public List<TopUserDto> countTopUsers() {
        List<User> listUsers = userService.findAll();
        List<TopUserDto> counts = new ArrayList<>();

        for (User user : listUsers) {
            counts.add(new TopUserDto(user.getUsername(), cartRepository.countByUserIdAndDone(user.getId(), true)));
        }

        counts = counts.stream().sorted(Comparator.comparingLong(TopUserDto::getCount)).collect(Collectors.toList());

        return counts.subList(counts.size() - 3, counts.size());
    }
}
