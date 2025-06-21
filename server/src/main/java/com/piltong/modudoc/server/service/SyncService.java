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
        log.info("SyncService initialized");
    }

    // 클라이언트 변경사항 반영 및 브로드캐스트
    // docId : ㅂsenderId : 보낸 사람 아이디

    /**
     * 클라이언트로부터 수신한 Operation을 이용해 server.document.content를 client.document.content와 동기화한다.
     * @param docId : 수정할 문서의 아이디
     * @param operation : 수정할 Operation
     * @param senderId : 전송자의 유저 아이디
     */
    public synchronized void syncUpdate(Integer docId, Operation operation, String senderId) {
        log.info("sync update : {}, {}, {}",  docId, operation, senderId);
        if (docId == null || operation == null || senderId == null) {
            String errMsg = "Invalid docId, dto or senderId";
            log.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        // 현재 문서 가져오기
        Document doc;
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

        String content = doc.getContent();

        // 수정사항이 null 값일 시 오류
        if (content == null) content = "";

        // 클라이언트에서 ?보낸 DTO를 실제 Operation 객체로 변환
//        Operation op;
//        try {
//            op = OperationMapper.toEntity(dto);
//            // op 객체로 변환 실패 시 오류
//            if (op == null) {
//                log.warn("OperationDto -> Operation 변환 실패");
//                return;
//            }
//        } catch (Exception e) {
//            log.warn("Operation 변환 중 예외 발생: {}", e.getMessage());
//            return;
//        }

        Operation op = operation;

        // 문서별 히스토리가 없다면 새로 생성
        operationHistory.putIfAbsent(docId, new ArrayList<>());
        List<Operation> history = operationHistory.get(docId);


        /**
         * 예외 발생 원인 -> Opration 결과 이후 또 Operation 수신 시, 결과 위치가 달라지는 오류 발생.
         * 하나의 List<Operation> 만을 수신했다면, 송신한 클라의 document가 제일 최신이라고 가정해야한다.
         */
        // 이전 모든 연산을 기준으로 현재 연산 위치 조정(OT 알고리즘 적용)
//        Operation transformedOp;
//        try {
//            transformedOp = ot.transformAgainstAll(operation, history);
//            if (transformedOp == null) {
//                System.err.println("[Error] OT transform 결과가 null");
//                return;
//            }
//        } catch (Exception e) {
//            System.err.println("[Error] OT transform 과정에서 예외 발생: " + e);
//            e.printStackTrace();
//            return;
//        }
        Operation transformedOp = operation;

        // 무시 연산 일 경우 처리 중단
        if (transformedOp.getPosition() == -1) {
            log.info("무시된 연산(Skip)");
            return;
        }

        // 문서 내용 적용 및 저장
        String updated;
        try {
            updated = ot.apply(content, transformedOp);
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
