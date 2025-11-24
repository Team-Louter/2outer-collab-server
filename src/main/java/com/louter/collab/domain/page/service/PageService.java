package com.louter.collab.domain.page.service;


import com.louter.collab.domain.page.dto.request.PageBlockEditRequest;
import com.louter.collab.domain.page.dto.request.PageCreateRequest;
import com.louter.collab.domain.page.dto.request.PageUpdateRequest;
import com.louter.collab.domain.page.dto.response.PageBlockResponse;
import com.louter.collab.domain.page.dto.response.PageResponse;

import java.util.List;

public interface PageService {
    PageResponse create(PageCreateRequest request);

    PageResponse update(Long pageId, PageUpdateRequest request);

    PageResponse getPage(Long pageId);

    List<PageResponse> getPages(Long teamId);

    void deletePage(Long pageId);


    PageBlockResponse createBlock(Long pageId, PageBlockCreateRequest request);

    PageBlockResponse editBlock(Long pageId, Long blockId, PageBlockEditRequest request);

    List<PageBlockResponse> getBlocks(Long pageId);

    void deleteBlock(Long pageId, Long blockId);
}
