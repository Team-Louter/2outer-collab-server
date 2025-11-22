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

    List<PageBlockResponse> getBlocks(Long pageId);

    PageBlockResponse editBlock(Long pageId, Long blockId, PageBlockEditRequest request);
}
