package com.fabiola.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fabiola.backend.entities.User;
import com.fabiola.backend.entities.UserAddress;
import com.fabiola.backend.repository.UserAddressRepository;

@Service
public class UserAddressService {

    private final UserAddressRepository userAddressRepository;

    @Autowired
    public UserAddressService(UserAddressRepository userAddressRepository) {
        this.userAddressRepository = userAddressRepository;
    }

    public Optional<UserAddress> findById(UUID id) {
        return userAddressRepository.findById(id);
    }

    public List<UserAddress> findByUser(User user) {
        return userAddressRepository.findByUser(user);
    }

    public UserAddress save(UserAddress userAddress) {
        return userAddressRepository.save(userAddress);
    }

    public void deleteById(UUID id) {
        userAddressRepository.deleteById(id);
    }
}
