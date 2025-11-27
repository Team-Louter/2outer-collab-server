package com.louter.collab.domain.report.service.impl;

import com.louter.collab.domain.page.entity.PageBlock;
import com.louter.collab.domain.report.dto.response.ReportResponse;
import com.louter.collab.domain.report.repository.ReportRepository;
import com.louter.collab.domain.report.service.ReportService;
import com.louter.collab.global.common.exception.EmptyContentException;
import com.louter.collab.global.common.exception.EmptyPageBlockException;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final ChatModel chatModel;

    @Override
    public ReportResponse summarizePage(Long pageId) {
        // index순으로 조회
        List<PageBlock> blocks = reportRepository.findByPageIdOrderByOrderIndexAsc(pageId);
        if (blocks.isEmpty()) {
            throw new EmptyPageBlockException("해당 페이지의 블록이 존재하지 않습니다");
        }

        // 블록 내용을 하나의 문자열로 변환
        String wholeContent = blocks.stream()
                .filter(block -> block.getContent() != null && !block.getContent().trim().isEmpty())
                .map(PageBlock::getContent)
                .collect(Collectors.joining("\n"));

        if (wholeContent.trim().isEmpty()) {
            throw new EmptyContentException("요약할 내용이 없습니다.");
        }

        // 3. OpenAI API를 사용하여 요약
        String text = generateSummary(wholeContent);

        return ReportResponse.builder()
                .pageId(pageId)
                .text(text)
                .build();    }

    private String generateSummary(String content) {
        String promptText = String.format(
                "다음은 회의록 내용입니다. 이 내용을 간결하고 명확하게 요약해주세요.\n\n" +
                        "요약 시 다음 사항을 포함해주세요:\n" +
                        "1. 회의 주제\n" +
                        "2. 주요 논의 사항\n" +
                        "3. 결정된 사항\n\n" +
                        "회의록 내용:\n%s",
                content
        );

        try {
            UserMessage userMessage = new UserMessage(promptText);
            Prompt prompt = new Prompt(List.of(userMessage));

            ChatResponse response = chatModel.call(prompt);

            return response.getResult().getOutput().getContent();

        } catch (Exception e) {
            throw new RuntimeException("회의록 요약 중 오류가 발생했습니다");
        }
    }
}
