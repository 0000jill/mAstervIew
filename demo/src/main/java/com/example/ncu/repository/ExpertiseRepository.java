package com.example.ncu.repository;

import java.util.List;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.ncu.domain.Expertise;

@Repository
public class ExpertiseRepository {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public void save(Integer resume_id, Expertise e){
        try {
            System.out.println("EXECUTE INSERT expertise");
            jdbcTemplate.update("INSERT INTO expertise(resume_id, expertise_name, expertise_description) VALUES (?,?,?)",
                    resume_id, e.getExpertiseName(), e.getExpertiseDescription());
            System.out.println("Resume inserted successfully.");
        } catch (DataAccessException exc) {
            System.err.println("Failed to insert expertise: " + exc.getMessage());
        }
    }
	public List<Map<String, Object>> findById(Integer resume_id) {
        System.out.println("EXECUTE SELECT expertise");
        List<Map<String, Object>> e =jdbcTemplate.queryForList("Select * FROM `expertise` where `resume_id`= ?", resume_id );
        return e;
    }
	public int update(Integer expertise_id, Expertise e) {
        int newe = jdbcTemplate.update("UPDATE `expertise` SET expertise_name = ? ,expertise_description = ? WHERE `expertise_id`= ?",
        		e.getExpertiseName(), e.getExpertiseDescription(), expertise_id);
        return newe;
    }


}
