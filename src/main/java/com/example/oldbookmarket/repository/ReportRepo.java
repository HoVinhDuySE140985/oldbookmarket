package com.example.oldbookmarket.repository;

import com.example.oldbookmarket.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepo extends JpaRepository<Report, Long> {
}
