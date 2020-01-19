package pl.com.dariusz.giza.warehouse.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.dariusz.giza.warehouse.domain.User;
import pl.com.dariusz.giza.warehouse.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        setPassword(user);
        return userRepository.save(user);
    }

    private void setPassword(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
    }

}
