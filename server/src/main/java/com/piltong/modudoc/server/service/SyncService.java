package com.piltong.modudoc.server.service;

import com.piltong.modudoc.common.Document;
import com.piltong.modudoc.server.core.OT;

// 다수 클라이언트 간 동기화 관리
// 충돌 방지 및 동기화를 유지하는 역할
// 변경된 내용을 다른 클라이언트에게 브로드캐스트함
public class SyncService {
    // 문서의 저장 및 조회 담당 서비스
    private final DocumentService documentService;
    // 동시 편집 충돌을 해결하기 위한 OT
    private final OT operationalTransformer;

    // 생성자로 문서 서비스와 OT 초기화
    public SyncService() {
        this.documentService = new DocumentService();
        this.operationalTransformer = new OT();
    }

    // 클라이언트 변경사항 반영 및 브로드캐스트
    public synchronized void syncUpdate(String documentId, String operation, String senderId) {
        // 1. 현재 문서 가져오기
        Document doc = documentService.getDocument(documentId);
        if (doc == null) {
            System.err.println("[Sync Error] 문서를 찾을 수 없습니다: " + documentId);
            return;
        }

        // 2. OT로 변경 내용 반영
        String current = doc.getContent();
        String updated = operationalTransformer.transform(current, operation);

        // 3. 문서 내용 업데이트 및 저장
        doc.setContent(updated);
        doc.touch(); // 수정 시간 갱신
        documentService.saveDocument(doc);  // 새로 추가된 메서드

        // 4. 브로드캐스트
        broadcastToOthers(documentId, updated, senderId);
    }

    // 변경 내용을 모든 다른 사용자에게 전달
    // 실제 전송 로직은 네트워크 계층에서 해야됨
    private void broadcastToOthers(String documentId, String updated, String senderId) {
        // senderID 제외한 나머지 클라이언트에 전송
        System.out.println("[Broadcast] From: " + senderId + " Doc: " + documentId + " Updated: " + updated);
    }
}
