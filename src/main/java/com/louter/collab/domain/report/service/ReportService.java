package com.louter.collab.domain.report.service;

import com.louter.collab.domain.report.dto.response.ReportResponse;

public interface ReportService {
    ReportResponse summarizePage(Long pageId);
}
