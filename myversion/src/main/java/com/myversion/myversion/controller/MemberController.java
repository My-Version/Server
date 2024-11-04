package com.myversion.myversion.controller;

import com.myversion.myversion.domain.Member;
import com.myversion.myversion.dto.member.MemberRequestDTO;
import com.myversion.myversion.service.MemberService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // Create or Update
    @PostMapping("/register")
    public Member createMember(@RequestBody MemberRequestDTO memberRequestDTO) {
        Long id = memberRequestDTO.getId();
        String name = memberRequestDTO.getName();
        String pw = memberRequestDTO.getPw();

        Member member = new Member(id, pw, name);


        return memberService.saveMember(member);
    }

    // Read by ID
    @GetMapping("/{id}")
    public Optional<Member> getMember(@PathVariable Long id) {
        return memberService.findMemberById(id);
    }

    // Read All
    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.findAllMembers();
    }

    // Delete by ID
    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable Long id) {
        memberService.deleteMemberById(id);
    }
}
