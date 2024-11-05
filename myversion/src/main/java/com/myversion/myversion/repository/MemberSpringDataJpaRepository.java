package com.myversion.myversion.repository;

import com.myversion.myversion.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberSpringDataJpaRepository extends JpaRepository<Member, String>, MemberRepository{
}
