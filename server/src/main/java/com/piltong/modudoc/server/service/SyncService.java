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
        // 현재 문서 가져오기
        Document doc = documentService.getDocument(documentId);
        String current = doc.getContent();

        // 클라이언트에서 보낸 DTO를 실제 Operation 객체로 변환
        Operation op = Operation.toEntity(dto);

        // 문서별 히스토리가 없다면 새로 생성
        if (!operationHistory.containsKey(documentId)) {
            operationHistory.put(documentId, new ArrayList<>());
        }

        // 이전 모든 연산을 기준으로 현재 연산 위치 조정(OT 알고리즘 적용)
        Operation transformedOp = ot.transformAgainstAll(op, operationHistory.get(documentId));
        // 조정된 연산을 문자열에 적용
        String updated = ot.apply(current, transformedOp);

        // 문서 내용 갱신
        doc.setContent(updated);
        documentService.saveDocument(doc);
        operationHistory.get(documentId).add(transformedOp);

        // 브로드캐스트
        broadcastToOthers(documentId, updated, senderId);
    }

    // 변경 내용을 모든 다른 사용자에게 전달
    // 실제 전송 로직은 네트워크 계층에서 해야됨
    private void broadcastToOthers(String documentId, String updated, String senderId) {
        // senderID 제외한 나머지 클라이언트에 전송
        System.out.println("[Broadcast] From: " + senderId + " Doc: " + documentId + " Updated: " + updated);
    }
}
