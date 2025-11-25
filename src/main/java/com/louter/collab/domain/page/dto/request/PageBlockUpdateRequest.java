package com.louter.collab.domain.page.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PageBlockUpdateRequest {
    private Long blockId;
    private Long parentBlockId;
    private String content;
    private String type;
    private Integer orderIndex;
}
