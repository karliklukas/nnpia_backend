package nnpia.seme.controller.rest;

import nnpia.seme.model.Senior;
import nnpia.seme.model.User;
import nnpia.seme.service.SeniorService;
import nnpia.seme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api/senior")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class SeniorRestController {
    private SeniorService seniorService;


    @Autowired
    public SeniorRestController(SeniorService seniorService) {
        this.seniorService = seniorService;
    }

    //@GetMapping
    @RequestMapping(value = "/api/senior", method = RequestMethod.GET)
    public List<Senior> getAll() {
        return seniorService.findAll();
    }

    //@PostMapping
    @RequestMapping(value = "/public/senior", method = RequestMethod.POST)
    public int createSenior(@RequestBody Senior senior) {
        int seniorId = seniorService.createSenior(senior.getEmail(), senior.getUsername(), senior.getCity());
        System.out.println("createREst "+seniorId);
        return seniorId;
    }

    //@GetMapping("{email}")
    @RequestMapping(value = "/api/senior/{email}", method = RequestMethod.GET)
    public int getIdByEmail(@PathVariable("email") String email) {
        System.out.println(email+"aaaa");
        Senior s = seniorService.findByEmail(email);
        return s.getId();
        //cartService.completeOrder(id);
    }
}
