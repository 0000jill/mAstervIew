package com.example.ncu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ncu.domain.Career;
@org.springframework.stereotype.Repository
public interface CareerRepository extends JpaRepository<Career, Integer> {

}