package com.myversion.myversion.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myversion.myversion.domain.Member;
import com.myversion.myversion.dto.member.LoginRequestDTO;
import com.myversion.myversion.dto.member.MemberRequestDTO;
import com.myversion.myversion.service.MemberService;


@RestController
@RequestMapping
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public Member Register(@RequestBody MemberRequestDTO memberRequestDTO) {
        String userId = memberRequestDTO.getUserId();
        String password = memberRequestDTO.getPassword();
        
        Member member = new Member(userId, password);

        return memberService.saveMember(member);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            String memberId = memberService.login(loginRequestDTO.getId(), loginRequestDTO.getPassword());
            return ResponseEntity.ok(memberId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }
    }

     public Optional<Member> getMember(String id) {
        return memberService.findMemberById(id);
    }

    public List<Member> getAllMembers() {
        return memberService.findAllMembers();
    }

    public void deleteMember(String id) {
        memberService.deleteMemberById(id);
    }
}
