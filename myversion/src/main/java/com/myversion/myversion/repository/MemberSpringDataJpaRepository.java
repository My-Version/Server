package com.myversion.myversion.repository;

import com.myversion.myversion.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSpringDataJpaRepository extends JpaRepository<Member, Long>{
}
