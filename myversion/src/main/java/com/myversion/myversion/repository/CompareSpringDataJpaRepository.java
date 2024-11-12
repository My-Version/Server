package com.myversion.myversion.repository;

import com.myversion.myversion.domain.Compare;
import com.myversion.myversion.domain.CoverSong;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompareSpringDataJpaRepository extends JpaRepository<Compare, Long> {
    @Query("SELECT c FROM Compare c WHERE c.userId = :userId")
    List<Compare> findAllByUserid(@Param("userId") String userId);
}
