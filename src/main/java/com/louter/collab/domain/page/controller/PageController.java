package com.louter.collab.domain.page.controller;

import com.louter.collab.domain.page.dto.request.PageBlockEditRequest;
import com.louter.collab.domain.page.dto.request.PageCreateRequest;
import com.louter.collab.domain.page.dto.request.PageUpdateRequest;
import com.louter.collab.domain.page.dto.response.PageBlockResponse;
import com.louter.collab.domain.page.dto.response.PageResponse;
import com.louter.collab.domain.page.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pages")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @PostMapping
    public PageResponse create(@RequestBody PageCreateRequest request) {
        return pageService.create(request);
    }

    @PutMapping("/{pageId}")
    public PageResponse update(
            @PathVariable Long pageId,
            @RequestBody PageUpdateRequest request) {
        return pageService.update(pageId, request);
    }

    @GetMapping("/{pageId}/blocks")
    public List<PageBlockResponse> getBlocks(@PathVariable Long pageId) {
        return pageService.getBlocks(pageId);
    }

    @PutMapping("/{pageId}/blocks/{blockId}")
    public PageBlockResponse editBlock(
            @PathVariable Long pageId,
            @PathVariable Long blockId,
            @RequestBody PageBlockEditRequest request) {

        return pageService.editBlock(pageId, blockId, request);
    }
}
