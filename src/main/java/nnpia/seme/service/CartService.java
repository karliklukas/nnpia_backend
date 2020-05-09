package nnpia.seme.service;

import javafx.util.Pair;
import nnpia.seme.dao.CartDao;
import nnpia.seme.dao.CartItemDao;
import nnpia.seme.dao.CartPaggingRepository;
import nnpia.seme.model.Cart;
import nnpia.seme.model.CartItem;
import nnpia.seme.model.TopUserDto;
import nnpia.seme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private long totalPages;

    //@Autowired
    private CartDao cartDao;

    @Autowired
    private CartPaggingRepository cartPagging;

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

    public List<Cart> findAllDoneByUser(Integer id) {
        User user = userService.findById(id);
        return cartDao.findAll().stream().filter(cart -> cart.isDone() && cart.getUser().equals(user)).collect(Collectors.toList());
    }

    public List<Cart> findAllWaitingByUser(Integer id) {
        User user = userService.findById(id);
        return cartDao.findAll().stream().filter(cart -> !cart.isDone() && cart.getUser().equals(user)).collect(Collectors.toList());
    }

    public Long countFreeCarts() {
        return cartDao.countByUserIdAndDone(null, false);
    }

    public Long countDoneByUser(Integer id) {
        return cartDao.countByUserIdAndDone(id, true);
    }

    public Long countWaitingByUser(Integer id) {
        return cartDao.countByUserIdAndDone(id, false);
    }

    public Cart findById(Integer id) {
        if (cartDao.findById(id).isPresent()) {
            return cartDao.findById(id).get();
        } else {
            throw new NoSuchElementException("Order with ID: " + id + " was not found!");
        }
    }

    public List<CartItem> getAllItems(Integer id) {
        Cart cart = findById(id);
        return new ArrayList<>(cart.getItems());
    }

    public void setCartDone(Integer id) {
        Cart cart = findById(id);
        cart.setDone(true);
        cartDao.save(cart);
    }

    public Cart setCartToUser(Integer idCart, String username) {
        User user = userService.findOne(username);
        Cart cart = cartDao.getOne(idCart);
        cart.setUser(user);
        return cartDao.save(cart);
    }

    public void addItemToCart(String item) {
        itemList.add(item);
    }

    public Integer completeOrder(Integer id) {
        Cart cart = new Cart();
        cart.setDone(false);
        cart.setSenior(seniorService.findById(id));
        cart = cartDao.save(cart);

        for (String item : itemList) {
            cartItemService.addCartItem(item, cart);
        }

        itemList.clear();
        return cart.getId();
    }

    public List<Cart> getAllFreeCartsPaSo(Integer pageNo, Integer pageSize, String sortBy, Integer userId, Boolean done) {
        Pageable paging;
        switch (sortBy) {
            case "time":
                paging = PageRequest.of(pageNo, pageSize, Sort.by("idcart").ascending());
                break;
            case "timeDesc":
                paging = PageRequest.of(pageNo, pageSize, Sort.by("idcart").descending());
                break;
            case "city":
                paging = PageRequest.of(pageNo, pageSize, Sort.by("senior.city"));
                break;
            default:
                paging = PageRequest.of(pageNo, pageSize);
                break;
        }
        Page<Cart> pagedResult;
        if (userId==-1){
            pagedResult = cartPagging.findAllByUserIdAndDone(null, done ,paging);
        }else{
            pagedResult = cartPagging.findAllByUserIdAndDone(userId, done ,paging);
        }
        totalPages = pagedResult.getTotalElements();

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Cart>();
        }
    }

    public List<TopUserDto> countTopUsers(){
        List<User> listUsers = userService.findAll();
        List<TopUserDto> counts = new ArrayList<>();

        for (User user: listUsers) {
            counts.add(new TopUserDto(user.getUsername(), cartDao.countByUserIdAndDone(user.getId(), true)));
        }

        counts = counts.stream().sorted((o1, o2) -> Long.compare(o1.getCount(), o2.getCount())).collect(Collectors.toList());
        counts.subList(0, 3).clear();
        for (TopUserDto user: counts) {
            System.out.println(user.getUsername()+" "+user.getCount());

        }


        return counts;
    }



    public long getTotalPages() {
        return totalPages;
    }
}
