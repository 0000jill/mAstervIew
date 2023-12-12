package com.example.ncu.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.ncu.domain.Special;

@Repository
public class SpecialRepository {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

	public void save(Integer resume_id, Special s){
        try {
            System.out.println("EXECUTE INSERT special");
            jdbcTemplate.update("INSERT INTO special(resume_id, special_name, special_description) VALUES (?,?,?)",
                    resume_id, s.getSpecialName(), s.getSpecialDescription());
            System.out.println("Special inserted successfully.");
        } catch (DataAccessException exc) {
            System.err.println("Failed to insert special: " + exc.getMessage());
        }
    }
	public List<Map<String, Object>> findById(Integer resume_id) {
        System.out.println("EXECUTE SELECT special");
        List<Map<String, Object>> s =jdbcTemplate.queryForList("Select * FROM `special` where `resume_id`= ?", resume_id );
        return s;
    }
	public int update(Integer special_id, Special s) {
        int news = jdbcTemplate.update("UPDATE `special` SET special_name = ? ,special_description = ? WHERE `special_id`= ?",
        		s.getSpecialName(), s.getSpecialDescription(), special_id);
        return news;
    }


}

