package com.example.ncu.service_impl;

import com.example.ncu.Exception.ResourceNotFoundException;
import com.example.ncu.domain.Career;
import com.example.ncu.repository.CareerRepository;
import com.example.ncu.service.CareerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class CareerService_impl implements CareerService {

    @Autowired
    private CareerRepository career_Repository;

    public CareerService_impl(CareerRepository career_Repository) {
        super();
        this.career_Repository = career_Repository;
    }

    @Override
    public List<Career> getAllCareers() {

        try {
            List<Career> careers = career_Repository.findAll();
            List<Career> customCareers = new ArrayList<>();
            careers.stream().forEach(e -> {
                Career career = new Career();
                BeanUtils.copyProperties(e, career);
                customCareers.add(career);
            });
            return customCareers;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Career getCareerById(int careerId) {
        Optional<Career> model = career_Repository.findById(careerId);
        if (model.isPresent()) {
            return model.get();
        } else {
            throw new ResourceNotFoundException("Career", "careerId", careerId);
        }

    }

}