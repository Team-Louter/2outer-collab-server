package com.louter.collab.domain.report.controller;

import com.louter.collab.domain.report.dto.request.ReportRequest;
import com.louter.collab.domain.report.dto.response.ReportResponse;
import com.louter.collab.domain.report.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponse> summarizePage(@Valid @RequestBody ReportRequest request) {
        ReportResponse response = reportService.summarizePage(request.getPageId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{pageId}")
    public ResponseEntity<ReportResponse> summarizePageByPath(@PathVariable Long pageId) {
        ReportResponse response = reportService.summarizePage(pageId);

        return ResponseEntity.ok(response);
    }
}
