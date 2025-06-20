package com.piltong.modudoc.server.service;

import com.piltong.modudoc.common.model.DocumentDto;
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

                return (R) new DocumentDto(
                        doc.getId(),
                        doc.getTitle(),
                        doc.getContent(),
                        doc.getCreatedDate(),
                        doc.getModifiedDate()
                );
            }

            // 문서 조회 요청 처리
            case READ_DOCUMENT:
                if (!(payload instanceof Integer docId)) {
                    throw new CommandException("READ_DOCUMENT: 잘못된 payload 타입입니다.");
                }
                Document document = documentService.findById((Integer) payload);
                log.info("문서 조회 성공: id={}", document.getId());
                return (R) document;

            // 문서 수정 요청 처리
            case UPDATE_DOCUMENT: {
                if (!(payload instanceof DocumentDto dto)) {
                    log.error("UPDATE_DOCUMENT: payload 타입 오류");
                    throw new CommandException("UPDATE_DOCUMENT: 잘못된 payload 타입입니다.");
                }
                if (!documentService.exists(dto.getId())) {
                    log.error("UPDATE_DOCUMENT: 존재하지 않는 문서: id={}", dto.getId());
                    throw new CommandException("존재하지 않는 문서입니다.");
                }

                // DocumentDto -> Document 변환
                Document doc = new Document(
                        dto.getId(),
                        dto.getTitle(),
                        dto.getContent(),
                        dto.getCreatedDate(),
                        dto.getModifiedDate()
                );

                documentService.update(doc);
                log.info("문서 수정: id={}", doc.getId());
                return (R) doc;
            }

            // 문서 삭제 요청 처리
            case DELETE_DOCUMENT:
                if (!(payload instanceof Integer docId)) {
                    log.error("DELETE_DOCUMENT: payload 타입 오류");
                    throw new CommandException("DELETE_DOCUMENT: 잘못된 payload 타입입니다.");
                }

                documentService.delete(docId);
                log.info("문서 삭제: id={}", docId);
                return null;

            // 문서 조회 요청 처리 - 본문(content) 없이
            case READ_DOCUMENT_LIST: {
                List<Document> allDocs = documentService.findAll();

                List<DocumentDto> dtoList = allDocs.stream()
                        .map(doc -> new DocumentDto(
                                doc.getId(),
                                doc.getTitle(),
                                null,
                                doc.getCreatedDate(),
                                doc.getModifiedDate()
                        )).toList();

                log.info("문서 요약 리스트 조회 완료({}개)", dtoList.size());
                return (R) dtoList;
            }

            // 클라이언트 편집 연산 동기화 요청 처리
            case PROPAGATE_OPERATION:
                if (!(payload instanceof OperationDto opDto)) {
                    log.error("PROPAGATE_OPERATION: payload 타입 오류");
                    throw new CommandException("PROPAGATE_OPERATION: 잘못된 payload 타입입니다.");
                }

                Integer docId = opDto.getDocId();

                if (!documentService.exists(docId)) {
                    log.error("PROPAGATE_OPERATION: 문서 존재하지 않음: id={}", docId);
                    throw new CommandException("해당 문서가 존재하지 않습니다.");
                }

                log.info("동기화 시작: docID={}, fromClient=null", docId);
                syncService.syncUpdate(docId, opDto, null);
                log.info("동기화 완료: docId={}", docId);
                return null;

            // 정의되지 않은 커맨드 처리
            default:
                log.error("정의되지 않은 커맨드 수신: {}", command);
                throw new CommandException("알 수 없는 커맨드: " + command);
        }
    }

    // 네트워크 오류 발생 시 호출됨
    @Override
    public void onNetworkError(Throwable t) {
        log.error("네트워크 오류 발생: " + t.getMessage());
        t.printStackTrace();
    }


}
