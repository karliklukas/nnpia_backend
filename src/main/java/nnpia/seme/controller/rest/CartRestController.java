package nnpia.seme.controller.rest;

import nnpia.seme.model.*;
import nnpia.seme.service.CartService;
import nnpia.seme.service.EmailService;
import nnpia.seme.service.SeniorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
//@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class CartRestController {

    private final CartService cartService;
    private final SeniorService seniorService;

    @Autowired
    public EmailService emailService;

    @Autowired
    public CartRestController(CartService cartService, SeniorService seniorService) {
        this.cartService = cartService;
        this.seniorService = seniorService;
    }

    @RequestMapping(value = "/api/cart/p", method = RequestMethod.GET)
    public ApiResponse<List<Cart>> getAllFreeCartsPaggingSorting(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "idcart") String sortBy,
            @RequestParam(defaultValue = "-1") Integer userId,
            @RequestParam(defaultValue = "false") Boolean done)
    {

        List<Cart> list = cartService.getAllFreeCartsPaSo(pageNo, pageSize, sortBy, userId, done);
        Long totalItems = cartService.getTotalPages();
        System.out.println(list.size()+" aaaa"+totalItems);

        return new ApiResponse<>(HttpStatus.OK.value(), ""+totalItems, list);
    }

    //@GetMapping("{id}")
    @RequestMapping(value = "/api/cart/{id}", method = RequestMethod.GET)
    public Cart getCartById(@PathVariable("id") Integer id) {
        System.out.println("aaaaaaa");
        return cartService.findById(id);
    }

    @RequestMapping(value = "/api/cart/done/{id}", method = RequestMethod.GET)
    public List<Cart> getDoneCartByUserId(@PathVariable("id") Integer id) {
        return cartService.findAllDoneByUser(id);
    }

    @RequestMapping(value = "/api/cart/waiting/{id}", method = RequestMethod.GET)
    public List<Cart> getWaitingCartByUserId(@PathVariable("id") Integer id) {
        return cartService.findAllWaitingByUser(id);
    }

    @RequestMapping(value = "/api/cart/count", method = RequestMethod.GET)
    public ApiResponse<List<Long>> getCountsCartByUserId(@RequestParam Integer id) {
        List<Long> list = new ArrayList<>();

        list.add(cartService.countFreeCarts());
        list.add(cartService.countDoneByUser(id));
        list.add(cartService.countWaitingByUser(id));

        return new ApiResponse<>(HttpStatus.OK.value(), "Success.",list);
    }

    @RequestMapping(value = "/api/cart/done", method = RequestMethod.GET)
    public ApiResponse<Boolean> setCartDone(@RequestParam Integer id) {
        cartService.setCartDone(id);
        return new ApiResponse<>(HttpStatus.OK.value(), "Cart is done.",true);
    }

    @RequestMapping(value = "/api/cart/take", method = RequestMethod.GET)
    public ApiResponse<Boolean> setCartToUser(
            @RequestParam Integer id,
            @RequestParam String username) {
        Cart cart = cartService.setCartToUser(id, username);
        Senior senior = cart.getSenior();
        User user = cart.getUser();
        System.out.println(senior.getEmail()+" "+user.getUsername()+" "+user.getEmail());
        Thread t = new Thread(() -> {
            try {
                emailService.sendSimpleMessage(senior.getEmail(), "BuyForYou - list ID: "+id+" was taken by "+user.getUsername(),
                        "Hello!\nYour list was taken by user "+user.getUsername()+". If you need you can contact him with email "+user.getEmail()+"\nStay home.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();

        return new ApiResponse<>(HttpStatus.OK.value(), "Cart taken successfully.",true);
    }

    //@PostMapping
    @RequestMapping(value = "/public/cart/", method = RequestMethod.POST)
    public ApiResponse<Boolean> createCart(@RequestBody CartDto cartDto) {
        int seniorId = seniorService.createSenior(cartDto.getSenior().getEmail(), cartDto.getSenior().getUsername(), cartDto.getSenior().getCity());

        for (String s: cartDto.getItemList()) {
            cartService.addItemToCart(s);
            System.out.println(s);
        }
        Integer cartId = cartService.completeOrder(seniorId);

        Thread t = new Thread(() -> {
            try {
                emailService.sendSimpleMessage(cartDto.getSenior().getEmail(), "BuyForYou - list added ID: "+cartId,
                        "Thank you!\n Your list was added and now just wait for volunteer. You can check your order status on our site with order ID: "+cartId+"\n Stay home.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();

        return new ApiResponse<>(HttpStatus.OK.value(), "Cart saved successfully. List ID:"+cartId,true);
    }

    @RequestMapping(value = "/public/cart/status", method = RequestMethod.GET)
    public ApiResponse<StatusDto> getCartStatus(@RequestParam Integer id) {
        System.out.println(id+" idcko");
        try {
            Cart cart = cartService.findById(id);
            StatusDto statusDto = new StatusDto();
            statusDto.setDone(cart.isDone());
            statusDto.setHaveUser(cart.getUser() != null);

            return new ApiResponse<>(HttpStatus.OK.value(), "success",statusDto);
        }catch (NoSuchElementException e){
            return new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(),null);
        }
    }


}
