package com.fabiola.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fabiola.backend.entities.User;
import com.fabiola.backend.entities.UserAddress;

public interface UserAddressRepository extends JpaRepository<UserAddress, UUID>{
    List<UserAddress> findByUser(User user);
}
