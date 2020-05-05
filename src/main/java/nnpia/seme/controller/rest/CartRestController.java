package nnpia.seme.controller.rest;

import nnpia.seme.model.Cart;
import nnpia.seme.model.CartGet;
import nnpia.seme.service.CartService;
import nnpia.seme.service.SeniorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@RestController
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
//@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:3000")
public class CartRestController {

    private final CartService cartService;
    private SeniorService seniorService;

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

    //@PostMapping
    @RequestMapping(value = "/public/cart", method = RequestMethod.POST)
    public boolean createCart(@RequestBody CartGet cartGet) {
        //System.out.println("popsis "+text);
        //cartService.addItemToCart(text);
        System.out.println("cartSenID"+cartGet.getId());
        System.out.println(cartGet.getItemList().size());

        for (String s: cartGet.getItemList()) {
            System.out.println(s);
            cartService.addItemToCart(s);
        }
        cartService.completeOrder(cartGet.getId());

        return true;
    }


}
