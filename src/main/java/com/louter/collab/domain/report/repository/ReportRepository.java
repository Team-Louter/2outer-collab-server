package com.louter.collab.domain.report.repository;

import com.louter.collab.domain.page.entity.PageBlock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends CrudRepository<PageBlock, Long> {
    // 인덱스 순서대로 조회
    @Query("SELECT pb FROM PageBlock pb WHERE pb.page.pageId = :pageId ORDER BY pb.orderIndex ASC")
    List<PageBlock> findByPageIdOrderByOrderIndexAsc(@Param("pageId") Long pageId);
}
