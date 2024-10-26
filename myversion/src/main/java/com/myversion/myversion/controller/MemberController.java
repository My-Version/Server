package com.myversion.myversion.controller;

import com.myversion.myversion.domain.Member;
import com.myversion.myversion.service.MemberService;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // Create or Update
    @PostMapping
    public Member createMember(@RequestBody Member member) {
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
