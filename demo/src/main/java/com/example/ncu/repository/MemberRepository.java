package com.example.ncu.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ncu.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    
	Member findByEmail(String email);
	
	Boolean existsByEmail(String email);
	

/*   AND a.password = :password
  
    @Query("SELECT s FROM member s WHERE s.name LIKE :name%")
    List<Member> findByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query("DELETE FROM member e WHERE e.name LIKE %:name%")
    void deleteByName(@Param("name") String name);
*/
}
