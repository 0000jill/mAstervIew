package com.example.ncu.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ncu.domain.Resume;
import com.example.ncu.repository.ResumeRepository;

@Service
public class ResumeService {
    @Autowired
    private ResumeRepository rr;

    public int createResume(Integer member_id, Resume r){
        int resume_id = rr.save(member_id, r);
        return resume_id;
    }

    public List<Map<String, Object>> findByMemberId(Integer member_id){
        return rr.findByMemberId(member_id);
    }
    public List<Map<String, Object>> findById(Integer resume_id){
        return rr.findById(resume_id);
    }
    public int update(Integer id, Resume r) {
        return rr.update(id, r);
    }
    public int findMemberId(Integer member_id) {
    	return rr.findMemberId(member_id);
    }

}
