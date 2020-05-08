package nnpia.seme.service;

import nnpia.seme.dao.UserDao;
import nnpia.seme.model.User;
import nnpia.seme.model.UserEditDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service(value = "userService")
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User findById(Integer id) {
        if (userDao.findById(id).isPresent()) {
            return userDao.findById(id).get();
        } else {
            throw new NoSuchElementException("Product with ID: " + id + " was not found!");
        }
    }

    public User findByEmail(String email) {
        if (userDao.findByEmail(email) != null) {
            return userDao.findByEmail(email);
        } else {
            throw new NoSuchElementException("Product with ID: " + email + " was not found!");
        }
    }

    public User findOne(String username) {
        return userDao.findByUsername(username);
    }

    public User addUser(String email, String username, String password) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(bcryptEncoder.encode(password));
        return userDao.save(user);
    }

    public boolean updatePassword(UserEditDto userIn) {
        User user = findOne(userIn.getUsername());

        if (user != null) {
            if (bcryptEncoder.matches(userIn.getPasswordOld(), user.getPassword())) {
                user.setPassword(bcryptEncoder.encode(userIn.getPasswordNew()));
                userDao.save(user);
                return true;
            }
        }
        return false;
    }
}
