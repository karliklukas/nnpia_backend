package nnpia.seme.controller.rest;

import nnpia.seme.model.ApiResponse;
import nnpia.seme.model.Cart;
import nnpia.seme.model.CartGet;
import nnpia.seme.service.CartService;
import nnpia.seme.service.SeniorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@RestController
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
//@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class CartRestController {

    private final CartService cartService;
    private final SeniorService seniorService;

    @Autowired
    public CartRestController(CartService cartService, SeniorService seniorService) {
        this.cartService = cartService;
        this.seniorService = seniorService;
    }

    //@GetMapping
    @RequestMapping(value = "/api/cart", method = RequestMethod.GET)
    public List<Cart> getFreeCart() {
        System.out.println("aaaaaaa");
        return cartService.findAllFreeCards();
    }

    @RequestMapping(value = "/api/cart/p", method = RequestMethod.GET)
    public ApiResponse<List<Cart>> getAllCartsPaggingSorting(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "idcart") String sortBy)
    {

        List<Cart> list = cartService.getAllFreeCartsPaSo(pageNo, pageSize, sortBy);
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

    @RequestMapping(value = "/api/cart/done/{id}", method = RequestMethod.POST)
    public void setCartDone(@PathVariable("id") Integer id) {
        cartService.setCartDone(id);
    }

    @RequestMapping(value = "/api/cart/take", method = RequestMethod.GET)
    public ApiResponse<Boolean> setCartToUser(
            @RequestParam Integer id,
            @RequestParam String username) {
        cartService.setCartToUser(id, username);
        return new ApiResponse<>(HttpStatus.OK.value(), "Cart taken successfully.",true);
    }

    //@PostMapping
    @RequestMapping(value = "/public/cart", method = RequestMethod.POST)
    public ApiResponse<Boolean> createCart(@RequestBody CartGet cartGet) {
        int seniorId = seniorService.createSenior(cartGet.getSenior().getEmail(), cartGet.getSenior().getUsername(), cartGet.getSenior().getCity());

        for (String s: cartGet.getItemList()) {
            cartService.addItemToCart(s);
        }
        cartService.completeOrder(seniorId);

        return new ApiResponse<>(HttpStatus.OK.value(), "Cart saved successfully.",true);
    }


}
