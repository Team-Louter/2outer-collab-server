package com.louter.collab.domain.notice.service.impl;

import com.louter.collab.domain.auth.entity.User;
import com.louter.collab.domain.auth.repository.UserRepository;
import com.louter.collab.domain.notice.dto.request.NoticeCreateRequest;
import com.louter.collab.domain.notice.dto.request.NoticeUpdateRequest;
import com.louter.collab.domain.notice.dto.response.NoticeResponse;
import com.louter.collab.domain.notice.entity.Notice;
import com.louter.collab.domain.notice.entity.NoticeCheck;
import com.louter.collab.domain.notice.repository.NoticeCheckRepository;
import com.louter.collab.domain.notice.repository.NoticeRepository;
import com.louter.collab.domain.notice.service.NoticeService;
import com.louter.collab.global.common.exception.NoticeNotFoundException;
import com.louter.collab.global.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeCheckRepository noticeCheckRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public NoticeResponse createNotice(NoticeCreateRequest request) {
        Notice notice = Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        return NoticeResponse.from(noticeRepository.save(notice));
    }

    @Override
    @Transactional
    public NoticeResponse updateNotice(Long noticeId, NoticeUpdateRequest request) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeNotFoundException("해당 공지를 찾을 수 없습니다 : " + noticeId));

        notice.update(request.getTitle(), request.getContent());

        return NoticeResponse.from(notice);
    }

    @Override
    @Transactional
    public void deleteNotice(Long noticeId) {
        noticeRepository.deleteById(noticeId);
    }

    @Override
    public List<NoticeResponse> getNotices() {
        return noticeRepository.findAll().stream()
                .map(NoticeResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public void checkNotice(Long noticeId, Long userId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeNotFoundException("공지 없음: " + noticeId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자 없음: " + userId));

        if (noticeCheckRepository.existsByNoticeIdAndUserId(noticeId, userId)) {
            return;
        }

        // 3. NoticeCheck 엔티티 생성 및 저장
        NoticeCheck check = NoticeCheck.builder()
                .notice(notice)
                .user(user)
                .build();

        noticeCheckRepository.save(check);
    }

}
