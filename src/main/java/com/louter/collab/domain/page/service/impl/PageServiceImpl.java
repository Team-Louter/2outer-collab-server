package com.louter.collab.domain.page.service.impl;

import com.louter.collab.domain.auth.entity.User;
import com.louter.collab.domain.auth.repository.UserRepository;
import com.louter.collab.domain.page.dto.request.PageBlockUpdateRequest;
import com.louter.collab.domain.page.dto.request.PageCreateRequest;
import com.louter.collab.domain.page.dto.response.PageBlockResponse;
import com.louter.collab.domain.page.dto.response.PageResponse;
import com.louter.collab.domain.page.entity.Page;
import com.louter.collab.domain.page.entity.PageBlock;
import com.louter.collab.domain.page.entity.PageChange;
import com.louter.collab.domain.page.repository.PageBlockRepository;
import com.louter.collab.domain.page.repository.PageChangeRepository;
import com.louter.collab.domain.page.repository.PageRepository;
import com.louter.collab.domain.page.service.PageService;
import com.louter.collab.domain.team.entity.Team;
import com.louter.collab.domain.team.repository.TeamRepository;
import com.louter.collab.global.common.exception.PageNotFoundException;
import com.louter.collab.global.common.exception.TeamNotFoundException;
import com.louter.collab.global.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;
    private final PageBlockRepository pageBlockRepository;
    private final PageChangeRepository pageChangeRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Override
    public PageResponse createPage(Long teamId, Long userId, PageCreateRequest request) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("Team not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Page page = Page.builder()
                .team(team)
                .title(request.getTitle())
                .author(user)
                .build();

        return PageResponse.from(pageRepository.save(page));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse getPage(Long pageId) {
        Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new PageNotFoundException("Page not found"));
        return PageResponse.from(page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PageResponse> getPagesByTeam(Long teamId) {
        return pageRepository.findByTeamTeamId(teamId).stream()
                .map(PageResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<PageBlockResponse> updateBlock(Long pageId, Long userId, PageBlockUpdateRequest request) {
        Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new PageNotFoundException("Page not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        PageBlock block;
        boolean isNew = false;
        
        if (request.getBlockId() != null) {
            block = pageBlockRepository.findById(request.getBlockId())
                    .orElseThrow(() -> new IllegalArgumentException("Block not found"));
            
            // Record change
            PageChange change = PageChange.builder()
                    .block(block)
                    .editor(user)
                    .changeContent(request.getContent())
                    .build();
            pageChangeRepository.save(change);
            
        } else {
            isNew = true;
            block = PageBlock.builder()
                    .page(page)
                    .author(user)
                    .build();
        }

        if (request.getParentBlockId() != null) {
            PageBlock parent = pageBlockRepository.findById(request.getParentBlockId())
                    .orElse(null);
            block.setParentBlock(parent);
        }

        block.setContent(request.getContent());
        block.setType(request.getType());
        
        if (isNew && request.getOrderIndex() != null) {
            // Shift existing blocks down
            pageBlockRepository.incrementOrderIndex(pageId, request.getOrderIndex());
            block.setOrderIndex(request.getOrderIndex());
            pageBlockRepository.save(block);
            
            // Return all affected blocks so frontend can update their order
            List<PageBlock> affectedBlocks = pageBlockRepository.findByPagePageIdAndOrderIndexGreaterThanEqualOrderByOrderIndexDesc(pageId, request.getOrderIndex());
            return affectedBlocks.stream()
                    .map(PageBlockResponse::from)
                    .collect(Collectors.toList());
        } else if (!isNew && request.getOrderIndex() != null && !request.getOrderIndex().equals(block.getOrderIndex())) {
            // Reordering existing block
            int oldIndex = block.getOrderIndex();
            int newIndex = request.getOrderIndex();
            
            if (newIndex > oldIndex) {
                pageBlockRepository.decrementOrderIndexRange(pageId, oldIndex, newIndex);
            } else {
                pageBlockRepository.incrementOrderIndexRange(pageId, newIndex, oldIndex);
            }
            block.setOrderIndex(newIndex);
            pageBlockRepository.save(block);
            
            // Return affected blocks
            int start = Math.min(oldIndex, newIndex);
            int end = Math.max(oldIndex, newIndex);
            List<PageBlock> affectedBlocks = pageBlockRepository.findByPagePageIdAndOrderIndexBetween(pageId, start, end);
            return affectedBlocks.stream()
                    .map(PageBlockResponse::from)
                    .collect(Collectors.toList());
        } else {
            block.setOrderIndex(request.getOrderIndex());
            return List.of(PageBlockResponse.from(pageBlockRepository.save(block)));
        }
    }

    @Override
    public void deleteBlock(Long pageId, Long userId, Long blockId) {
        PageBlock block = pageBlockRepository.findById(blockId)
                .orElseThrow(() -> new IllegalArgumentException("Block not found"));
        
        // Check if block belongs to page
        if (!block.getPage().getPageId().equals(pageId)) {
            throw new IllegalArgumentException("Block does not belong to this page");
        }

        Integer deletedOrderIndex = block.getOrderIndex();

        // Delete associated changes first to avoid FK constraint violation
        pageChangeRepository.deleteByBlock(block);

        pageBlockRepository.delete(block);
        
        // Shift subsequent blocks up
        pageBlockRepository.decrementOrderIndexAfter(pageId, deletedOrderIndex);
    }
}
