package com.example.ncu.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.ncu.domain.Workexp;

@Repository
public class WorkexpRepository {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public void save(Integer resume_id, Workexp w){
        try {
            System.out.println("EXECUTE INSERT workexp");
            jdbcTemplate.update("INSERT INTO workexp(resume_id, workexp_name, work_period, workexp_description) VALUES (?,?,?,?)",
                    resume_id, w.getWorkexpName(), w.getWorkPeriod(),  w.getWorkexpDescription());
            System.out.println("Workexp inserted successfully.");
        } catch (DataAccessException exc) {
            System.err.println("Failed to insert workexp: " + exc.getMessage());
        }
    }
	public List<Map<String, Object>> findById(Integer resume_id) {
        System.out.println("EXECUTE SELECT expertise");
        List<Map<String, Object>> e =jdbcTemplate.queryForList("Select * FROM `workexp` where `resume_id`= ?", resume_id );
        return e;
    }
	public int update(Integer workexp_id, Workexp w) {
        int newe = jdbcTemplate.update("UPDATE `workexp` SET workexp_name = ? ,work_period = ? ,workexp_description = ? WHERE `workexp_id`= ?",
        		w.getWorkexpName(), w.getWorkPeriod(),  w.getWorkexpDescription(), workexp_id);
        return newe;
    }

}
