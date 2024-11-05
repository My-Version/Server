package com.myversion.myversion.controller;

import com.myversion.myversion.domain.Member;
import com.myversion.myversion.dto.member.LoginRequestDTO;
import com.myversion.myversion.dto.member.MemberRequestDTO;
import com.myversion.myversion.service.MemberService;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        String id = memberRequestDTO.getId();
        String name = memberRequestDTO.getName();
        String pw = memberRequestDTO.getPassword();

        Member member = new Member(id, pw, name);

        return memberService.saveMember(member);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            Member member = memberService.login(loginRequestDTO.getId(), loginRequestDTO.getPassword());
            return ResponseEntity.ok(member); // 로그인 MEMBER 객체 반환
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // Read by ID
    @GetMapping("/{id}")
    public Optional<Member> getMember(@PathVariable String id) {
        return memberService.findMemberById(id);
    }

    // Read All
    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.findAllMembers();
    }

    // Delete by ID
    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable String id) {
        memberService.deleteMemberById(id);
    }
}
