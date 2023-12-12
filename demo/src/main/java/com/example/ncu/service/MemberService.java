package com.example.ncu.service;

import jakarta.transaction.Transactional;

import java.util.List;

import com.example.ncu.domain.Member;

public interface MemberService {

    Member saveMember(Member member);
    Member getMemberById(int memberId);
    Member updateMemberById(Member member, int memberId) ;
    boolean authenticateUser(String email, String password);
	boolean existsByEmail(String email);
	Member getMemberByEmail(String email);
	void updateMemberProfile(int memberId, String newPhotoPath);
	String getProfilePhotoPath(int memberId);
    
/*
    List<Member> getAllMembers();
    Member updateMemberByName(Member member, String name);
    void deleteMemberById(int memberId);
    void deleteMemberByName(String name);
*/
}