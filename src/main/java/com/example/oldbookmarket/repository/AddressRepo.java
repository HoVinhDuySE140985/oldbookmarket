package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address, Long> {
    Address findAddressByUser_Id(Long id);
}
