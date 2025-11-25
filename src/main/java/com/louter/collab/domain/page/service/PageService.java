package com.louter.collab.domain.page.service;

import com.louter.collab.domain.page.dto.request.PageBlockUpdateRequest;
import com.louter.collab.domain.page.dto.request.PageCreateRequest;
import com.louter.collab.domain.page.dto.response.PageBlockResponse;
import com.louter.collab.domain.page.dto.response.PageResponse;

import java.util.List;

public interface PageService {
    PageResponse createPage(Long teamId, Long userId, PageCreateRequest request);
    PageResponse getPage(Long pageId);
    List<PageResponse> getPagesByTeam(Long teamId);
    List<PageBlockResponse> updateBlock(Long pageId, Long userId, PageBlockUpdateRequest request);
    void deleteBlock(Long pageId, Long userId, Long blockId);
}
