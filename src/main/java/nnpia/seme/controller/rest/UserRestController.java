package nnpia.seme.controller.rest;

import nnpia.seme.model.ApiResponse;
import nnpia.seme.model.User;
import nnpia.seme.service.CartService;
import nnpia.seme.service.SeniorService;
import nnpia.seme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserRestController {

    private UserService userService;


    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    //@GetMapping
    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public List<User> getAll() {
        return userService.findAll();
    }

    //@GetMapping("{email}")
    @RequestMapping(value = "/api/user/{email}", method = RequestMethod.GET)
    public User getUserByEmail(@PathVariable("email") String email) {
        return userService.findByEmail(email);
    }

    //@PostMapping
    @RequestMapping(value = "/public/user", method = RequestMethod.POST)
    public ApiResponse<User> addUser(@RequestBody User user) {
        System.out.println(user.getEmail()+" "+ user.getUsername()+" "+user.getPassword());
        if(userService.findOne(user.getUsername())==null){
            return new ApiResponse<>(HttpStatus.OK.value(), "User saved successfully.", userService.addUser(user.getEmail(), user.getUsername(), user.getPassword()));
        }else {
            return new ApiResponse<>(HttpStatus.CONFLICT.value(), "Username already exist.", null);
        }
    }


}
