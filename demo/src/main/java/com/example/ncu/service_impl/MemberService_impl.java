package com.example.ncu.service_impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ncu.Exception.ResourceNotFoundException;
import com.example.ncu.domain.Member;
import com.example.ncu.repository.MemberRepository;
import com.example.ncu.service.MemberService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService_impl implements MemberService {

	@Autowired
    private final MemberRepository memberRepository;


    @Autowired
    public MemberService_impl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    };
    
    @Override
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    };
    
    @Override
    public boolean authenticateUser(String email, String password) {
        Member m = memberRepository.findByEmail(email);
        if (m != null && m.getPassword().equals(password)) {
            return true; // 密碼驗證成功
        }
        return false; // 驗證失敗
    }
    
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
    
    @Override
    public Member getMemberById(int memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new ResourceNotFoundException("Member", "memberId", memberId);
        }

        // return member_Repository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("Member", "memberId", memberId));

    }
    
    @Override
    public String getProfilePhotoPath(int memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            return member.getProfileImage();
        }
        return null;
    }

    @Override
    public Member updateMemberById(Member member, int memberId) {

        // check whether member with given id is exist in DB or not
        Member existingMember = memberRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("Member", "memberId", memberId));

        existingMember.setName(member.getName());
        existingMember.setPassword(member.getPassword());
        existingMember.setEmail(member.getEmail());
        //save existing member to DB
        memberRepository.save(existingMember);
        return existingMember;
    }
    
    @Override
    public void updateMemberProfile(int memberId, String newPhotoPath) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member != null) {
            member.setProfileImage(newPhotoPath);
            memberRepository.save(member);
        } else {
            // 處理找不到該會員的情況
        	
        }
    }


    
    
/*
    @Override
    public List<Member> getAllMembers() {

        try {
            List<Member> members = member_Repository.findAll();
            List<Member> customMembers = new ArrayList<>();
            members.stream().forEach(e -> {
                Member member = new Member();
                BeanUtils.copyProperties(e, member);
                customMembers.add(member);
            });
            return customMembers;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void deleteMemberById(int memberId) {

        //check whether a member exists in a DB or not
        member_Repository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("Member", "memberId", memberId));
        member_Repository.deleteById(memberId);
    }
    
    @Override
    public void deleteMemberByName(String name) {

        //check whether a member exists in a DB or not
       // member_Repository.findByName(name);
        member_Repository.deleteByName(name);
    }
*/
}
