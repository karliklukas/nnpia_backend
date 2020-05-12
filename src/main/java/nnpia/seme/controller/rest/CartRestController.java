package nnpia.seme.controller.rest;

import nnpia.seme.dto.*;
import nnpia.seme.model.*;
import nnpia.seme.service.CartService;
import nnpia.seme.service.EmailService;
import nnpia.seme.service.SeniorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/cart")
public class CartRestController {

    private final CartService cartService;
    private final EmailService emailService;

    @Autowired
    public CartRestController(CartService cartService, EmailService emailService) {
        this.cartService = cartService;
        this.emailService = emailService;
    }

    @GetMapping
    public ApiResponse<CartPagingDto> getAllFreeCartsPagingSorting(
            @RequestParam(defaultValue = "-1") Integer userId,
            @RequestParam(defaultValue = "false") Boolean done, Pageable pageable) {

        CartPagingDto list = cartService.getAllFreeCartsPaSo(pageable, userId, done);
        return new ApiResponse<>(HttpStatus.OK.value(), "", list);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Cart getCartById(@PathVariable("id") Integer id) {
        return cartService.findById(id);
    }

    @RequestMapping(value = "/done/{id}", method = RequestMethod.GET)
    public List<Cart> getDoneCartByUserId(@PathVariable("id") Integer id) {
        return cartService.findAllDoneByUser(id);
    }

    @RequestMapping(value = "/waiting/{id}", method = RequestMethod.GET)
    public List<Cart> getWaitingCartByUserId(@PathVariable("id") Integer id) {
        return cartService.findAllWaitingByUser(id);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public ApiResponse<MainPageDto> getCountsCartByUserId(@RequestParam Integer id) {
        MainPageDto counts = new MainPageDto();
        counts.setCountFree(cartService.countFreeCarts());
        counts.setCountDone(cartService.countDoneByUser(id));
        counts.setCountWait(cartService.countWaitingByUser(id));

        return new ApiResponse<>(HttpStatus.OK.value(), "Success.", counts);
    }

    @RequestMapping(value = "/done", method = RequestMethod.GET)
    public ApiResponse<Boolean> setCartDone(@RequestParam Integer id) {
        cartService.setCartDone(id);
        return new ApiResponse<>(HttpStatus.OK.value(), "Cart is done.", true);
    }

    @RequestMapping(value = "/take", method = RequestMethod.GET)
    public ApiResponse<Boolean> setCartToUser(
            @RequestParam Integer id,
            @RequestParam String username) {

        Cart cart = cartService.setCartToUser(id, username);
        Senior senior = cart.getSenior();
        User user = cart.getUser();

        Thread t = new Thread(() -> {
            try {
                emailService.sendSimpleMessage(senior.getEmail(), "BuyForYou - list ID: " + id + " was taken by " + user.getUsername(),
                        "Hello!\nYour list was taken by user " + user.getUsername() + ". If you need you can contact him with email " + user.getEmail() + "\nStay home.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();

        return new ApiResponse<>(HttpStatus.OK.value(), "Cart taken successfully.", true);
    }


}
