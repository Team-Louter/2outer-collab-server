package com.louter.collab.domain.page.repository;

import com.louter.collab.domain.page.entity.PageBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PageBlockRepository extends JpaRepository<PageBlock, Long> {
    List<PageBlock> findByPagePageIdOrderByOrderIndexAsc(Long pageId);

    @Modifying
    @Query("UPDATE PageBlock b SET b.orderIndex = b.orderIndex + 1 WHERE b.page.pageId = :pageId AND b.orderIndex >= :orderIndex")
    void incrementOrderIndex(@Param("pageId") Long pageId, @Param("orderIndex") Integer orderIndex);

    @Modifying
    @Query("UPDATE PageBlock b SET b.orderIndex = b.orderIndex + 1 WHERE b.page.pageId = :pageId AND b.orderIndex >= :start AND b.orderIndex < :end")
    void incrementOrderIndexRange(@Param("pageId") Long pageId, @Param("start") Integer start, @Param("end") Integer end);

    @Modifying
    @Query("UPDATE PageBlock b SET b.orderIndex = b.orderIndex - 1 WHERE b.page.pageId = :pageId AND b.orderIndex > :start AND b.orderIndex <= :end")
    void decrementOrderIndexRange(@Param("pageId") Long pageId, @Param("start") Integer start, @Param("end") Integer end);

    @Modifying
    @Query("UPDATE PageBlock b SET b.orderIndex = b.orderIndex - 1 WHERE b.page.pageId = :pageId AND b.orderIndex > :orderIndex")
    void decrementOrderIndexAfter(@Param("pageId") Long pageId, @Param("orderIndex") Integer orderIndex);

    List<PageBlock> findByPagePageIdAndOrderIndexGreaterThanEqualOrderByOrderIndexDesc(Long pageId, Integer orderIndex);
    
    List<PageBlock> findByPagePageIdAndOrderIndexBetween(Long pageId, Integer start, Integer end);
}
