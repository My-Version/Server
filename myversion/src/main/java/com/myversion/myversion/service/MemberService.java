package com.myversion.myversion.service;

import com.myversion.myversion.domain.Member;
import com.myversion.myversion.repository.MemberSpringDataJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberSpringDataJpaRepository memberRepository;

    @Autowired
    public MemberService(MemberSpringDataJpaRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // Create or Update
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    // Find by ID
    public Optional<Member> findMemberById(Long id) {
        return memberRepository.findById(id);
    }

    // Find All
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    // Delete by ID
    public void deleteMemberById(Long id) {
        memberRepository.deleteById(id);
    }

}
