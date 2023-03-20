package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepo extends JpaRepository<Address, Long> {
    List<Address> findAllByUser_Id(Long id);

    Address findAddressByIdAndUserId(Long addressId, Long userId);
}
