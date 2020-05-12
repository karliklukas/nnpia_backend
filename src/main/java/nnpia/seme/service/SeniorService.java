package nnpia.seme.service;

import nnpia.seme.dao.SeniorRepository;
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
    private SeniorRepository seniorRepository;

    public List<Senior> findAll() {
        return seniorRepository.findAll();
    }

    public Senior findById(Integer id) {
        if (seniorRepository.findById(id).isPresent()) {
            return seniorRepository.findById(id).get();
        } else {
            throw new NoSuchElementException("Senior with ID: " + id + " was not found!");
        }
    }

    public Senior findByEmail(String email) {
        if (seniorRepository.findByEmail(email) != null) {
            return seniorRepository.findByEmail(email);
        } else {
            throw new NoSuchElementException("Senior with email: " + email + " was not found!");
        }
    }

    public int createSenior(String email, String username, String city){
        Senior senior = new Senior();
        senior.setEmail(email);
        senior.setUsername(username);
        senior.setCity(city);
        senior = seniorRepository.saveAndFlush(senior);

        return senior.getId();
    }

}
