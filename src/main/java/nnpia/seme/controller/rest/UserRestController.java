package nnpia.seme.controller.rest;

import nnpia.seme.model.ApiResponse;
import nnpia.seme.model.User;
import nnpia.seme.dto.UserEditDto;
import nnpia.seme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/user")
public class UserRestController {

    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }

    @RequestMapping(value = "/{email}", method = RequestMethod.GET)
    public User getUserByEmail(@PathVariable("email") String email) {
        return userService.findByEmail(email);
    }

    @PutMapping
    public ApiResponse<Boolean> updatePassword(@RequestBody UserEditDto user) {
        System.out.println("put");
        if (userService.updatePassword(user)){
            return new ApiResponse<>(HttpStatus.OK.value(), "", "User updated successfully.");
        }else{
            return new ApiResponse<>(HttpStatus.CONFLICT.value(), "Wrong password.", false);
        }

    }
}
