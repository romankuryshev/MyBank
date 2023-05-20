package com.bankapp.mybank.Service;

import com.bankapp.mybank.Model.Role;
import com.bankapp.mybank.Model.User;
import com.bankapp.mybank.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean addUser(User user){
        User existUser = userRepository.findByUsername(user.getUsername());
        if(existUser != null){
            return false;
        }

        user.setActive(true);
        user.setRoles(Set.of(Role.USER));
        userRepository.save(user);
        return true;
    }

    public User getUserById(Integer userId){
        return userRepository.findByUserId(userId);
    }
}
