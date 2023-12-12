package com.example.ncu.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.ncu.domain.Resume;

@Repository
public class ResumeRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Integer member_id, Resume r){
        System.out.println("EXECUTE INSERT resume");
        jdbcTemplate.update("INSERT INTO resume(member_id, title, gender, age, hobby, school_name, academic, department_name, academic_period, status, preferred_type, working_hours, position) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    member_id, r.getTitle(), r.getGender(), r.getAge(), r.getHobby(), r.getSchoolName(), r.getAcademic(), r.getDepartmentName(), r.getAcademicPeriod(), r.getStatus(), r.getPreferredType(), r.getWorkingHours(), r.getPosition());

        // 插入資料後，查詢最新生成的 resume_id
        String query = "SELECT LAST_INSERT_ID()";
        int generatedResumeId = jdbcTemplate.queryForObject(query, Integer.class);
        return generatedResumeId;
    }
		

    public List<Map<String, Object>> findById(Integer resume_id) {
        System.out.println("EXECUTE SELECT resume");
        List<Map<String, Object>> r =jdbcTemplate.queryForList("Select * FROM `resume` where `resume_id`= ?", resume_id );
        return r;
    }
    public List<Map<String, Object>> findByMemberId(Integer member_id) {
        System.out.println("EXECUTE SELECT resume");
        List<Map<String, Object>> r =jdbcTemplate.queryForList("Select `title`, `member_id`, `resume_id` FROM `resume` where `member_id`= ?", member_id );
        return r;
        
    }
    public int update(Integer id, Resume r) {
        int newr = jdbcTemplate.update("UPDATE `resume` SET gender = ? , age = ? ,hobby = ? ,school_name = ? ,academic = ? ,department_name = ? ,academic_period = ? ,status = ? ,preferred_type = ? ,working_hours = ?, position = ? WHERE `resume_id`= ?",
                r.getGender(), r.getAge(), r.getHobby(), r.getSchoolName(), r.getAcademic(), r.getDepartmentName(), r.getAcademicPeriod(), r.getStatus(), r.getPreferredType(), r.getWorkingHours(), r.getPosition(), id);
        return newr;
    }
    public int findMemberId(Integer resume_id) {
    	int id = jdbcTemplate.queryForObject("select member_id FROM `resume` where `resume_id` = ?",
    			Integer.class,  // 注意這裡的 Class<Integer> 作為第三個參數
    	        resume_id);
    	return id;
    }


}
