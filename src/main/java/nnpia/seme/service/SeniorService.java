package nnpia.seme.service;

import nnpia.seme.dao.SeniorDao;
import nnpia.seme.model.Senior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class SeniorService {
    @Autowired
    private SeniorDao seniorDao;

    private  Senior senior;

    public List<Senior> findAll() {
        return seniorDao.findAll();
    }

    public Senior findById(Integer id) {
        if (seniorDao.findById(id).isPresent()) {
            return seniorDao.findById(id).get();
        } else {
            throw new NoSuchElementException("Product with ID: " + id + " was not found!");
        }
    }

    public Senior findByEmail(String email) {
        if (seniorDao.findByEmail(email) != null) {
            return seniorDao.findByEmail(email);
        } else {
            throw new NoSuchElementException("Product with emial: " + email + " was not found!");
        }
    }

    public int createSenior(String email, String username, String city){
        senior = new Senior();
        senior.setEmail(email);
        senior.setUsername(username);
        senior.setCity(city);
        senior = seniorDao.saveAndFlush(senior);
        System.out.println("seniorServ "+senior.getId());
        return senior.getId();
    }

}
