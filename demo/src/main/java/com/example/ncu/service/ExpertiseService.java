package com.example.ncu.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ncu.domain.Expertise;
import com.example.ncu.repository.ExpertiseRepository;

@Service
public class ExpertiseService {
	@Autowired
	private ExpertiseRepository er;
	
	public void createExpertise(Integer resume_id, Expertise e){
        er.save(resume_id, e);
    }
    public List<Map<String, Object>> findById(Integer resume_id){
        return er.findById(resume_id);
    }
    public int update(Integer expertise_id, Expertise e) {
        return er.update(expertise_id, e);
    }
	

}
