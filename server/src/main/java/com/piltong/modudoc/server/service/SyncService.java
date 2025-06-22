package com.piltong.modudoc.server.service;

import com.piltong.modudoc.server.model.Document;
import com.piltong.modudoc.common.model.OperationDto;
import com.piltong.modudoc.server.core.OT;
import com.piltong.modudoc.server.model.Operation;
import com.piltong.modudoc.server.model.OperationMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

// 다수 클라이언트 간 동기화 관리
// OT 알고리즘을 통해 충돌을 해결
// 변경된 내용을 다른 클라이언트에게 브로드캐스트함
public class SyncService {
    private static final Logger log = LogManager.getLogger(SyncService.class); // 로거
    // 문서의 저장 및 조회 담당 서비스
    private final DocumentService docService;
    // 동시 편집 충돌을 해결하기 위한 OT
    private final OT ot;
    // 편집 기록을 저장하는 히스토리 맵
    private final Map<Integer, List<Operation>> operationHistory = new HashMap<>();

    // 생성자: 문서 서비스와 OT 초기화
    public SyncService(DocumentService docService) {
        this.docService = docService;
        this.ot = new OT();
    }

    // 클라이언트 변경사항 반영 및 브로드캐스트
    public synchronized void syncUpdate(Integer docId, OperationDto dto, String senderId) {
        // null 값의 파라미터가 들어올 시 오류
        if (docId == null || dto == null || senderId == null) {
            log.error("Invalid docId, dto or senderId");
            throw new RuntimeException("Invalid docId, dto or senderId");
        }

        // 현재 문서 가져오기
        Document doc;
        // Document가 null 값일 시 오류
        try {
            doc = docService.findById(docId);
            if (doc == null) {
                log.error("Invalid doc");
                throw new RuntimeException("Invalid doc");
            }
        } catch (Exception e) {
            log.error("Exception on syncUpdate: {}", e.getMessage());
            throw new RuntimeException("Exception on syncUpdate" + e.getMessage());
        }

        String current = doc.getContent();

        // 수정사항이 null 값일 시 오류
        if (current == null) current = "";

        // 클라이언트에서 보낸 DTO를 실제 Operation 객체로 변환
        Operation op;
        try {
            op = OperationMapper.toEntity(dto);
            // op 객체로 변환 실패 시 오류
            if (op == null) {
                log.warn("OperationDto -> Operation 변환 실패");
                return;
            }
        } catch (Exception e) {
            log.warn("Operation 변환 중 예외 발생: {}", e.getMessage());
            return;
        }

        // 문서별 히스토리가 없다면 새로 생성
        operationHistory.putIfAbsent(docId, new ArrayList<>());
        List<Operation> history = operationHistory.get(docId);

        // 이전 모든 연산을 기준으로 현재 연산 위치 조정(OT 알고리즘 적용)
        Operation transformedOp;
        try {
            transformedOp = ot.transformAgainstAll(op, history);
            if (transformedOp == null) {
                log.warn("OT transform 결과가 null");
                return;
            }
        } catch (Exception e) {
            log.warn("OT transform 중 예외 발생: {}", e.getMessage());
            return;
        }

        // 무시 연산 일 경우 처리 중단
        if (transformedOp.getPosition() == -1) {
            log.info("무시된 연산(Skip)");
            return;
        }

        // 문서 내용 적용 및 저장
        String updated;
        try {
            updated = ot.apply(current, transformedOp);
            if (updated == null) {
                log.warn("apply() 결과가 null");
                return;
            }
            doc.setContent(updated);
            docService.update(doc);
        } catch (IllegalArgumentException e) {
            log.warn("apply 또는 저장 중 좌표 오류: {}", e.getMessage());
            return;
        } catch (Exception e) {
            log.error("문서 저장 실패: {}", e.getMessage());
            return;
        }

        // 연산 기록
        history.add(transformedOp);

        // 브로드캐스트
        try {
            broadcastToOthers(docId, updated, senderId);
        } catch (Exception e) {
            log.warn("브로드캐스트 중 예외 발생: {}", e.getMessage());
        }

    }

    // 변경 내용을 모든 다른 사용자에게 전달
    // 실제 전송 로직은 네트워크 계층에서 해야됨
    private void broadcastToOthers(Integer docId, String updated, String senderId) {
        // senderID 제외한 나머지 클라이언트에 전송
        try {
            if (updated == null || senderId == null) return;
            System.out.println("[Broadcast] From: " + senderId + " Doc: " + docId + " Updated: " + updated);
        } catch (Exception e) {
            System.out.println("[Broadcast] broadcast 실패 : " + e.getMessage());
        }
    }
    
    // Test용 문서 생성 메소드
    public void updateDocument(Integer docId, String title, String content) {
        docService.update(docId, title, content);
    }

    public Document getDocument(Integer docId) {
        return docService.findById(docId);
    }
}
