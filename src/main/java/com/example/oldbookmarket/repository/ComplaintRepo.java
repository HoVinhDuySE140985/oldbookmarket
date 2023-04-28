package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepo extends JpaRepository<Complaint, Long> {
    List<Complaint> findAllByOrder_CodeOrder(String orderCode);

}
