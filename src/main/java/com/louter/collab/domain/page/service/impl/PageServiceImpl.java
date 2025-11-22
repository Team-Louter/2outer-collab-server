package com.louter.collab.domain.page.service.impl;

import com.louter.collab.domain.auth.entity.User;
import com.louter.collab.domain.auth.repository.UserRepository;
import com.louter.collab.domain.page.dto.request.PageBlockEditRequest;
import com.louter.collab.domain.page.dto.request.PageCreateRequest;
import com.louter.collab.domain.page.dto.request.PageUpdateRequest;
import com.louter.collab.domain.page.dto.response.PageBlockResponse;
import com.louter.collab.domain.page.dto.response.PageResponse;
import com.louter.collab.domain.page.entity.Page;
import com.louter.collab.domain.page.entity.PageBlock;
import com.louter.collab.domain.page.repository.PageBlockRepository;
import com.louter.collab.domain.page.repository.PageChangeRepository;
import com.louter.collab.domain.page.repository.PageCollaboratorRepository;
import com.louter.collab.domain.page.repository.PageRepository;
import com.louter.collab.domain.page.service.PageService;
import com.louter.collab.domain.team.entity.Team;
import com.louter.collab.domain.team.repository.TeamRepository;
import com.louter.collab.global.common.exception.EditNotFoundException;
import com.louter.collab.global.common.exception.PageNotFoundException;
import com.louter.collab.global.common.exception.TeamNotFoundException;
import com.louter.collab.global.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PageServiceImpl implements PageService {
    private final PageChangeRepository pageChangeRepository;
    private final PageCollaboratorRepository pageCollaboratorRepository;
    private final PageRepository pageRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final PageBlockRepository pageBlockRepository;

    // 회의록 생성
    @Override
    @Transactional
    public PageResponse create(PageCreateRequest request) {

        // team 조회
        Team team = teamRepository.findByTeamId(request.getTeamId())
                .orElseThrow(() -> new TeamNotFoundException("해당 팀을 찾을 수 없습니다."));

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
                .orElseThrow(() -> new PageNotFoundException("해당 회의록을 찾을 수 없습니다."));

        page.update(request.getTitle());
        return PageResponse.from(page);
    }

    @Override
    @Transactional
    public List<PageBlockResponse> getBlocks(Long pageId) {
        Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new PageNotFoundException("해당 회의록을 찾을 수 없습니다."));

        return page.getBlocks().stream()
                .map(PageBlockResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public PageBlockResponse editBlock(Long pageId, Long blockId, PageBlockEditRequest request){

        PageBlock block = pageBlockRepository.findById(blockId)
                .orElseThrow(() -> new EditNotFoundException("해당 안건을 찾을 수 없습니다."));

        block.update(request.getContent(), request.getType(), request.getOrderIndex());

        return PageBlockResponse.from(block);
    }
}
