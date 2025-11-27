package com.louter.collab.domain.notice.controller;

import com.louter.collab.domain.notice.dto.request.NoticeCreateRequest;
import com.louter.collab.domain.notice.dto.request.NoticeUpdateRequest;
import com.louter.collab.domain.notice.dto.response.NoticeResponse;
import com.louter.collab.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    public NoticeResponse createNotice(@RequestBody NoticeCreateRequest request) {
        return noticeService.createNotice(request);
    }

    @GetMapping
    public List<NoticeResponse> getNotices() {
        return noticeService.getNotices();
    }

    @PutMapping("/{noticeId}")
    public NoticeResponse updateNotice(@PathVariable Long noticeId,
    @RequestBody NoticeUpdateRequest request)
    {
        return noticeService.updateNotice(noticeId, request);
    }


    @DeleteMapping("/{noticeId}")
    public void deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
    }
}
