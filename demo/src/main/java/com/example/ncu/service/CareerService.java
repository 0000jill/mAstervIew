package com.example.ncu.service;

import com.example.ncu.domain.Career;

import java.util.List;

public interface CareerService {

    List<Career> getAllCareers();

    Career getCareerById(int careerId);

}