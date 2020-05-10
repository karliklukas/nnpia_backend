package nnpia.seme.service;

import nnpia.seme.dao.UserRepository;
import nnpia.seme.model.User;
import nnpia.seme.model.UserEditDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service(value = "userService")
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        } else {
            throw new NoSuchElementException("Product with ID: " + id + " was not found!");
        }
    }

    public User findByEmail(String email) {
        if (userRepository.findByEmail(email) != null) {
            return userRepository.findByEmail(email);
        } else {
            throw new NoSuchElementException("Product with email: " + email + " was not found!");
        }
    }

    public User findOne(String username) {
        return userRepository.findByUsername(username);
    }

    public User addUser(String email, String username, String password) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(bcryptEncoder.encode(password));
        return userRepository.save(user);
    }

    public boolean updatePassword(UserEditDto userIn) {
        User user = findOne(userIn.getUsername());

        if (user != null) {
            if (bcryptEncoder.matches(userIn.getPasswordOld(), user.getPassword())) {
                user.setPassword(bcryptEncoder.encode(userIn.getPasswordNew()));
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}
