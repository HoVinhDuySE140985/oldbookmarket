package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepo extends JpaRepository<Complaint, Long> {
}
