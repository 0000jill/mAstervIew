package com.example.ncu.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ncu.domain.Workexp;
import com.example.ncu.repository.WorkexpRepository;

@Service
public class WorkexpService {
	
	@Autowired
	private WorkexpRepository wr;
	
	public void createWorkexp(Integer resume_id, Workexp w){
        wr.save(resume_id, w);
    }
    public List<Map<String, Object>> findById(Integer resume_id){
        return wr.findById(resume_id);
    }
    public int update(Integer workexp_id, Workexp w) {
        return wr.update(workexp_id, w);
    }

}
