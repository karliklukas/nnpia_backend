package nnpia.seme.controller;

import nnpia.seme.model.ApiResponse;
import nnpia.seme.model.User;
import nnpia.seme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
//@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    //@PostMapping
    @RequestMapping(value = "/users/dsa/dsa", method = RequestMethod.POST)
    public ApiResponse<Integer> saveUser(@RequestBody User user){
        System.out.println("dsadsa");
        return new ApiResponse<>(HttpStatus.OK.value(), "User saved successfully.",1);
    }



   /* @GetMapping
    public ApiResponse<List<User>> listUser(){
        return new ApiResponse<>(HttpStatus.OK.value(), "User list fetched successfully.",userService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getOne(@PathVariable int id){
        return new ApiResponse<>(HttpStatus.OK.value(), "User fetched successfully.",userService.findById(id));
    }*/




}
