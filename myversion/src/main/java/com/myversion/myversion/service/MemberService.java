package com.myversion.myversion.service;

import com.myversion.myversion.domain.Member;
import com.myversion.myversion.repository.MemberRepository;
import com.myversion.myversion.repository.MemberSpringDataJpaRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // Create or Update
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    public Member login(Long id, String password) {
        Optional<Member> member = memberRepository.findById(id);
        if (member.isPresent() && Objects.equals(member.get().getPassWord(), password)) {
            return member.get();
        }
        return null;
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
