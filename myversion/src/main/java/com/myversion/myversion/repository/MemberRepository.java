package com.myversion.myversion.repository;

import com.myversion.myversion.domain.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(String id);
    List<Member> findAll();
    void deleteById(String id);
}
