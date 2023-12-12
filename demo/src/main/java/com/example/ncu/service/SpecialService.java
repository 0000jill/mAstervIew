package com.example.ncu.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ncu.domain.Special;
import com.example.ncu.repository.SpecialRepository;

@Service
public class SpecialService {
	
	@Autowired
	private SpecialRepository sr;
	
	public void createSpecial(Integer resume_id, Special s){
        sr.save(resume_id, s);
    }
    public List<Map<String, Object>> findById(Integer resume_id){
        return sr.findById(resume_id);
    }
    public int update(Integer special_id, Special s) {
        return sr.update(special_id, s);
    }
	

}
