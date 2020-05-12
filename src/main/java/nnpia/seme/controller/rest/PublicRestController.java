package nnpia.seme.controller.rest;

import nnpia.seme.dto.CartDto;
import nnpia.seme.dto.StatusDto;
import nnpia.seme.dto.TopUserDto;
import nnpia.seme.model.ApiResponse;
import nnpia.seme.model.Cart;
import nnpia.seme.model.User;
import nnpia.seme.service.CartService;
import nnpia.seme.service.EmailService;
import nnpia.seme.service.SeniorService;
import nnpia.seme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/public")
public class PublicRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private SeniorService seniorService;
    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ApiResponse<User> addUser(@RequestBody User user) {
        System.out.println(user.getEmail()+" "+ user.getUsername()+" "+user.getPassword());
        if(userService.findOne(user.getUsername())==null){
            return new ApiResponse<>(HttpStatus.OK.value(), "User saved successfully.", userService.addUser(user.getEmail(), user.getUsername(), user.getPassword()));
        }else {
            return new ApiResponse<>(HttpStatus.CONFLICT.value(), "Username already exist.", null);
        }
    }

    @RequestMapping(value = "/cart", method = RequestMethod.POST)
    public ApiResponse<String> createCart(@RequestBody CartDto cartDto) {
        int seniorId = seniorService.createSenior(cartDto.getSenior().getEmail(), cartDto.getSenior().getUsername(), cartDto.getSenior().getCity());

        for (String s : cartDto.getItemList()) {
            cartService.addItemToCart(s);
        }
        Integer cartId = cartService.completeOrder(seniorId);

        Thread t = new Thread(() -> {
            try {
                emailService.sendSimpleMessage(cartDto.getSenior().getEmail(), "BuyForYou - list added ID: " + cartId,
                        "Thank you!\n Your list was added and now just wait for volunteer. You can check your order status on our site with order ID: " + cartId + "\n Stay home.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();

        return new ApiResponse<>(HttpStatus.OK.value(), "", "Cart saved successfully. List ID:" + cartId);
    }

    @RequestMapping(value = "/cart/status", method = RequestMethod.GET)
    public ApiResponse<StatusDto> getCartStatus(@RequestParam Integer id) {
        try {
            Cart cart = cartService.findById(id);
            StatusDto statusDto = new StatusDto();
            statusDto.setDone(cart.isDone());
            statusDto.setHaveUser(cart.getUser() != null);

            return new ApiResponse<>(HttpStatus.OK.value(), "success", statusDto);
        } catch (NoSuchElementException e) {
            return new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
        }
    }

    @RequestMapping(value = "/cart/top", method = RequestMethod.GET)
    public ApiResponse<List<TopUserDto>> getTopUsers() {
        return new ApiResponse<>(HttpStatus.OK.value(), "success", cartService.countTopUsers());
    }
}
