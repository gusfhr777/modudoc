package com.piltong.modudoc.server.service;

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
        // 현재 문서 내용 가져오기 및 OT 통해 수정된 내용
        String current = documentService.getDocument(documentId);
        String updated = operationalTransformer.transform(current, operation);

        // 변경된 내용을 문서에 반영
        documentService.updateDocument(documentId, updated);
        // 이후 senderId 외 다른 모든 유저에게 브로드캐스트
        broadcastToOthers(documentId, updated, senderId);
    }

    // 변경 내용을 모든 다른 사용자에게 전달
    // 실제 구현은 ServerNetworkHandler에서 할지 여기서 모르겠음
    private void broadcastToOthers(String documentId, String updated, String senderId) {
        // senderID 제외한 나머지 클라이언트에 전송
        System.out.println("[Broadcast] From: " + senderId + " Doc: " + documentId + " Updated: " + updated);
    }
}
