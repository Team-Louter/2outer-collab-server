package com.louter.collab.page.service.impl;

import com.louter.collab.auth.domain.User;
import com.louter.collab.auth.repository.UserRepository;
import com.louter.collab.common.exception.UserNotFoundException;
import com.louter.collab.page.domain.Page;
import com.louter.collab.page.dto.request.PageBlockEditRequest;
import com.louter.collab.page.dto.request.PageCreateRequest;
import com.louter.collab.page.dto.request.PageUpdateRequest;
import com.louter.collab.page.dto.response.PageBlockResponse;
import com.louter.collab.page.dto.response.PageResponse;
import com.louter.collab.page.repository.PageBlockRepository;
import com.louter.collab.page.repository.PageChangeRepository;
import com.louter.collab.page.repository.PageCollaboratorRepository;
import com.louter.collab.page.repository.PageRepository;
import com.louter.collab.page.service.PageService;
import com.louter.collab.team.domain.Team;
import com.louter.collab.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PageServiceImpl implements PageService {
    private final PageBlockRepository pageBlockRepository;
    private final PageChangeRepository pageChangeRepository;
    private final PageCollaboratorRepository pageCollaboratorRepository;
    private final PageRepository pageRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    // 회의록 생성
    @Override
    @Transactional
    public PageResponse create(PageCreateRequest request) {

        // team 조회
        Team team = teamRepository.findByTeamId(request.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("해당 팀을 찾을 수 없습니다."));

        // user 조회
        User author = userRepository.findByUserId(request.getAuthorId())
                .orElseThrow(() -> new UserNotFoundException("해당 작성자를 찾을 수 없습니다."));

        Page page = Page.builder()
                .team(team)
                .title(request.getTitle())
                .author(author)
                .build();

        return PageResponse.from(pageRepository.save(page));
    }

    @Override
    @Transactional
    public PageResponse update(Long pageId, PageUpdateRequest request) {
        Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회의록을 찾을 수 없습니다."));

        page.update(request.getTitle());
        return PageResponse.from(pageRepository.save(page));
    }

    @Override
    @Transactional
    public List<PageBlockResponse> getBlocks(Long pageId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional
    public PageBlockResponse editBlock(Long pageId, Long blockId, PageBlockEditRequest request){
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
