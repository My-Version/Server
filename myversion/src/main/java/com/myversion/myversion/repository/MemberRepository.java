package com.myversion.myversion.repository;

import com.myversion.myversion.domain.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(int id);
    Optional<Member> findByName(String name);
    List<Member> findAll();
}
