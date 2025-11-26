package com.louter.collab.domain.notice.service;

import com.louter.collab.domain.notice.dto.request.NoticeCreateRequest;
import com.louter.collab.domain.notice.dto.request.NoticeUpdateRequest;
import com.louter.collab.domain.notice.dto.response.NoticeResponse;

import java.util.List;

public interface NoticeService {
    NoticeResponse createNotice(NoticeCreateRequest request);
    NoticeResponse updateNotice(Long id, NoticeUpdateRequest request);
    void deleteNotice(Long id);
    List<NoticeResponse> getNotices();
}
