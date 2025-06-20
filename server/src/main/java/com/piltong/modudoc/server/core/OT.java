package com.piltong.modudoc.server.core;

import java.util.List;

import com.piltong.modudoc.common.model.OperationType;
import com.piltong.modudoc.server.model.Operation;

/** 추후 확장 기능
 * Undo 기능 (history가 필요함)
 * 문자열 전체 변경 기록 저장 (history)
 */

// 동시 편집 충돌 해결 알고리즘
public class OT {
    // 두 연산 간 충돌 해결
    public Operation[] transform(Operation prior, Operation current) {
        // 복사하여 원본은 유지
        Operation op1 = copy(prior);
        Operation op2 = copy(current);

        int len1 = op1.getContent() != null ? op1.getContent().length() : 0;
        int len2 = op2.getContent() != null ? op2.getContent().length() : 0;

        // 1. 둘 다 insert인 경우
        if (op1.getOperationType() == OperationType.INSERT && op2.getOperationType() == OperationType.INSERT) {
            // op1이 먼저 왔거나 같으면, op2의 위치를 len1만큼 밀어줌
            if (op1.getPosition() <= op2.getPosition())
                op2.setPosition(op2.getPosition() + len1);
                // 아니면 op1의 위치를 밀어줌
            else
                op1.setPosition(op1.getPosition() + len2);
        }
        // 2. insert, delete인 경우
        else if (op1.getOperationType() == OperationType.INSERT && op2.getOperationType() == OperationType.DELETE) {
            // insert가 delete 보다 앞이면 delete 위치 뒤로 조정
            if (op1.getPosition() <= op2.getPosition()) op2.setPosition(op2.getPosition() + len1);
            else op1.setPosition(op1.getPosition() - len2);    // 반대면 insert 위치를 len2만큼 당겨줌

        }
        // 3. delete, insert인 경우
        else if (op1.getOperationType() == OperationType.DELETE && op2.getOperationType() == OperationType.INSERT) {
            // delete가 insert 보다 앞이면 insert 위치 당겨줌
            if (op1.getPosition() < op2.getPosition()) op2.setPosition(op2.getPosition() - len1);
            else op1.setPosition(op1.getPosition() + len2);    // 반대면 delete 위치를 뒤로 조정

        }
        // 4. 둘 다 delete인 경우
        else if (op1.getOperationType() == OperationType.DELETE && op2.getOperationType() == OperationType.DELETE) {
            // op1이 앞이면 op2의 위치를 len1만큼 당겨줌
            if (op1.getPosition() < op2.getPosition()) op2.setPosition(op2.getPosition() - len1);
                // op2가 앞이면 op1의 위치를 len2만큼 당겨줌
            else if (op1.getPosition() > op2.getPosition()) op1.setPosition(op1.getPosition() - len2);
                // 같은 위치 삭제 시 둘 중 하나 무시 (op2 무시)
            else op2.setPosition(-1);
        }

        // 조정된 두 연산을 배열로 반환
        return new Operation[]{op1, op2};
    }

    // 여러 선행 연산에 대해 순차적으로 조정
    public Operation transformAgainstAll(Operation op, List<Operation> priorOps) {
        Operation result = copy(op);
        for (Operation prior : priorOps) {
            result = transform(prior, result)[1];
        }
        return result;
    }

    // 문자열에 연산 적용
    public String apply(String original, Operation op) {
        int pos = op.getPosition();
        if (pos < 0 || pos > original.length())  // ← 여기서 `>=`이 아니라 `>` 이어야 함
            throw new IllegalArgumentException("잘못된 위치: " + pos + " / 길이: " + original.length());

        return switch (op.getOperationType()) {
            case INSERT -> {
                String before = original.substring(0, pos);
                String after = (pos == original.length()) ? "" : original.substring(pos); // ← 핵심 수정
                yield before + op.getContent() + after;
            }
            case DELETE -> {
                int len = op.getContent() != null ? op.getContent().length() : 1;
                String before = original.substring(0, pos);
                String after = original.substring(pos + len);
                yield before + after;
            }
            default -> throw new UnsupportedOperationException(
                    "지원하지 않는 연산 타입: " + op.getOperationType());
        };
    }

    // Operation 복사
    private Operation copy(Operation op) {
        return new Operation(
                op.getOperationType(),
                op.getDocId(),
                op.getPosition(),
                op.getContent()
        );
    }
}