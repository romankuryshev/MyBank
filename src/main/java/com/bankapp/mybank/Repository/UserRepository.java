package com.bankapp.mybank.Repository;

import com.bankapp.mybank.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAll();
    User findByUsername(String username);
    User findByUserId(Integer userId);
}
