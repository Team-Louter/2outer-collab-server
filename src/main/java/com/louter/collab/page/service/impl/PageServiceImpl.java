package com.louter.collab.page.service.impl;

import com.louter.collab.page.repository.PageBlockRepository;
import com.louter.collab.page.repository.PageChangeRepository;
import com.louter.collab.page.repository.PageCollaboratorRepository;
import com.louter.collab.page.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PageServiceImpl {
    private final PageBlockRepository pageBlockRepository;
    private final PageChangeRepository pageChangeRepository;
    private final PageCollaboratorRepository pageCollaboratorRepository;
    private final PageRepository pageRepository;
}
