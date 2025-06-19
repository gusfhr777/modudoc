package com.piltong.modudoc.server.service;

import com.piltong.modudoc.common.document.Document;
import com.piltong.modudoc.common.operation.Operation;
import com.piltong.modudoc.common.operation.OperationDto;
import com.piltong.modudoc.server.core.OT;

import java.util.*;

// 다수 클라이언트 간 동기화 관리
// OT 알고리즘을 통해 충돌을 해결
// 변경된 내용을 다른 클라이언트에게 브로드캐스트함
public class SyncService {
    // 문서의 저장 및 조회 담당 서비스
    private final DocumentService documentService;
    // 동시 편집 충돌을 해결하기 위한 OT
    private final OT ot;
    // 편집 기록을 저장하는 히스토리 맵
    private final Map<String, List<Operation>> operationHistory = new HashMap<>();

    // 생성자: 문서 서비스와 OT 초기화
    public SyncService() {
        this.documentService = new DocumentService();
        this.ot = new OT();
    }

    // 클라이언트 변경사항 반영 및 브로드캐스트
    public synchronized void syncUpdate(String documentId, OperationDto dto, String senderId) {
        // null 값의 파라미터가 들어올 시 오류
        if (documentId == null || dto == null || senderId == null) {
            System.err.println("[Error] null 값의 파라미터가 입력됨");
            return;
        }

        // 현재 문서 가져오기
        Document doc;
        // Document가 null 값일 시 오류
        try {
            doc = documentService.getDocument(documentId);
            if (doc == null) {
                System.err.println("[Error] Document를 찾을수 없음: " + documentId);
                return;
            }
        } catch (IllegalArgumentException e) {
            System.err.println("[Error] getDocument() 예외: " + e);
            return;
        }

        String current = doc.getContent();

        // 수정사항이 null 값일 시 오류
        if (current == null) current = "";

        // 클라이언트에서 보낸 DTO를 실제 Operation 객체로 변환
        Operation op;
        try {
            op = Operation.toEntity(dto);
            // op 객체로 변환 실패 시 오류
            if (op == null) {
                System.err.println("[Error] OperationDto를 Operation 으로 변환 실패");
                return;
            }
        } catch (Exception e) {
            System.err.println("[Error] Operation 변환 중 예외 발생: " + e);
            return;
        }

        // 문서별 히스토리가 없다면 새로 생성
        operationHistory.putIfAbsent(documentId, new ArrayList<>());
        List<Operation> history = operationHistory.get(documentId);

        // 이전 모든 연산을 기준으로 현재 연산 위치 조정(OT 알고리즘 적용)
        Operation transformedOp;
        try {
            transformedOp = ot.transformAgainstAll(op, history);
            if (transformedOp == null) {
                System.err.println("[Error] OT transform 결과가 null");
                return;
            }
        } catch (Exception e) {
            System.err.println("[Error] OT transform 과정에서 예외 발생: " + e);
            e.printStackTrace();
            return;
        }

        // 무시 연산 일 경우 처리 중단
        if (transformedOp.getPosition() == -1) {
            System.out.println("무시된 연산(Skip)");
            return;
        }

        // 문서 내용 적용 및 저장
        String updated;
        try {
            updated = ot.apply(current, transformedOp);
            if (updated == null) {
                System.err.println("[Error] apply() 결과가 null");
                return;
            }
            doc.setContent(updated);
            documentService.saveDocument(doc);
        } catch (IllegalArgumentException e) {
            System.err.println("[Error] apply 또는 저장 중 좌표 오류: " + e);
            return;
        } catch (Exception e) {
            System.err.println("[Error] 문서 저장 실패: " + e);
            e.printStackTrace();
            return;
        }

        // 연산 기록
        history.add(transformedOp);

        // 브로드캐스트
        try {
            broadcastToOthers(documentId, updated, senderId);
        } catch (Exception e) {
            System.err.println("[Error] 브로드캐스트 중 예외 발생: " + e);
        }

    }

    // 변경 내용을 모든 다른 사용자에게 전달
    // 실제 전송 로직은 네트워크 계층에서 해야됨
    private void broadcastToOthers(String documentId, String updated, String senderId) {
        // senderID 제외한 나머지 클라이언트에 전송
        try {
            if (updated == null || senderId == null) return;
            System.out.println("[Broadcast] From: " + senderId + " Doc: " + documentId + " Updated: " + updated);
        } catch (Exception e) {
            System.out.println("[Broadcast] broadcast 실패 : " + e.getMessage());
        }
    }
    
    // Test용 문서 생성 메소드
    public void updateDocument(String documentId, String title, String content) {
        documentService.updateDocument(documentId, title, content);
    }

    public Document getDocument(String documentId) {
        return documentService.getDocument(documentId);
    }
}
