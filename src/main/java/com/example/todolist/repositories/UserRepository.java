package com.example.todolist.repositories;

import com.example.todolist.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository  extends JpaRepository<User, UUID> {
    User findByUsername(String username);
}
