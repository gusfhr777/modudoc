package com.piltong.modudoc.server.service;

import com.piltong.modudoc.common.model.OperationDto;
import com.piltong.modudoc.server.model.Document;
import com.piltong.modudoc.common.network.*;
import com.piltong.modudoc.server.network.ServerNetworkListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;

// 서버 측에서 클라이언트 요청을 처리하는 리스너 구현체
// 각 커맨드에 따라 알맞은 문서 서비스나 동기화 서비스를 호출하여 처리
public class ServerNetworkListenerImpl implements ServerNetworkListener {
    private static final Logger log = LogManager.getLogger(ServerNetworkListenerImpl.class);
    private final DocumentService documentService;  // 문서 저장/조회/수정/삭제 처리
    private final SyncService syncService;          // 실시간 편집 동기화 처리

    public ServerNetworkListenerImpl(DocumentService documentService, SyncService syncService) {
        this.documentService = documentService;
        this.syncService = syncService;
    }

    // 클라이언트로부터 명령을 수신했을 때 호출됨
    // 각 명령 유형에 따라 분기 처리
    @Override
    @SuppressWarnings("unchecked")
    public <T, R> R onCommandReceived(ClientCommand command, T payload) throws CommandException {
        switch (command) {

            // 문서 생성 요청 처리
            case CREATE_DOCUMENT: {
                if (!(payload instanceof String title)) {
                    String errMsg = "CREATE_DOCUMENT: 잘못된 payload 타입입니다.";
                    log.error(errMsg);
                    throw new CommandException(errMsg);
                }

                // content 지정
                String content = "";    // 빈 문자열로 생성

                Document doc = documentService.create(title, content);

                return (R) doc;
            }

            // 문서 조회 요청 처리
            case READ_DOCUMENT:
                if (!(payload instanceof Integer docId))
                    throw new CommandException("READ_DOCUMENT: 잘못된 payload 타입입니다.");
                Document Doc = documentService.find(docId);
                return (R) Document.toDto(Doc);

            // 문서 조회 요청 처리 - 본문(content) 없이
            case READ_DOCUMENT_SUMMARIES: {
                List<Document> allDocs = documentService.findAll();

                List<DocumentSummaryDto> summaries = allDocs.stream()
                        .map(doc -> new DocumentSummaryDto(
                        doc.getId(),
                        doc.getTitle(),
                        doc.getCreatedDate(),
                        doc.getModifiedDate(),
                        doc.getAccessUserIds()
                )).toList();

                return (R) summaries;
            }

            // 문서 수정 요청 처리
            case UPDATE_DOCUMENT: {
                if (!(payload instanceof DocumentSummaryDto dto))
                    throw new CommandException("UPDATE_DOCUMENT: 잫못된 payload 타입입니다.");
                if (!documentService.exists(dto.getId()))
                    throw new CommandException("존재하지 않는 문서입니다.");
                String currentContent = documentService.find(dto.getId()).getContent();
                documentService.update(dto.getId(), dto.getTitle(), currentContent);
                return (R) new DocumentSummaryDto(
                        dto.getId(),
                        dto.getTitle(),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of()
                );
            }

            // 문서 삭제 요청 처리
            case DELETE_DOCUMENT:
                if (!(payload instanceof String docId))
                    throw new CommandException("DELETE_DOCUMENT: 잘못된 payload 타입입니다.");
                documentService.removeDocument(docId);
                return (R) docId;

            // 클라이언트 편집 연산 동기화 요청 처리
            case PROPAGATE_OPERATION:
                if (!(payload instanceof OperationDto opDto))
                    throw new CommandException("PROPAGATE_OPERATION: 잘못된 payload 타입입니다.");
                if (!documentService.exists(opDto.findId()))
                    throw new CommandException("해당 문서가 존재하지 않습니다.");
                syncService.syncUpdate(opDto.findId(), opDto, "null");
                return null;

            // 정의되지 않은 커맨드 처리
            default:
                throw new CommandException("알 수 없는 커맨드: " + command);
        }
    }

    // 네트워크 오류 발생 시 호출됨
    // 현재 콘솔에만 출력. (추후 로깅 시스템으로 대체?)
    @Override
    public void onNetworkError(Throwable t) {
        System.err.println("네트워크 오류 발생: " + t.getMessage());
        t.printStackTrace();
    }
}
