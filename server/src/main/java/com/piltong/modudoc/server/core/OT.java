package com.piltong.modudoc.server.core;

import java.util.List;

import com.piltong.modudoc.common.operation.Operation;
import com.piltong.modudoc.common.operation.OperationType;

/** 추후 확장 기능
 * Undo 기능 (history가 필요함)
 * 문자열 전체 변경 기록 저장 (history)
 */

// 동시 편집 충돌 해결 알고리즘
public class OT {
    // 두 연산 간 충돌 해결
    public Operation[] transform(Operation op1, Operation op2) {
       // 복사하여 원본은 유지
        op1 = copy(op1);
        op2 = copy(op2);

        // 1. 둘 다 insert인 경우
        if (op1.getOperationType() == OperationType.INSERT && op2.getOperationType() == OperationType.INSERT) {
            // op1이 먼저 왔거나 같은 위치면, op2의 위치를 한 칸 밀어줌
            if (op1.getPosition() <= op2.getPosition()) op2.setPosition(op2.getPosition() + 1);
            else op1.setPosition(op1.getPosition() + 1);    // 반대면 op1을 한칸 밀어줌

        }
        // 2. insert, delete인 경우
        else if (op1.getOperationType() == OperationType.INSERT && op2.getOperationType() == OperationType.DELETE) {
            // insert가 delete 보다 앞이면 delete 위치 뒤로 조정
            if (op1.getPosition() <= op2.getPosition()) op2.setPosition(op2.getPosition() + 1);
            else op1.setPosition(op1.getPosition() - 1);    // 반대면 insert 위치를 한 칸 당겨줌

        }
        // 3. delete, insert인 경우
        else if (op1.getOperationType() == OperationType.DELETE && op2.getOperationType() == OperationType.INSERT) {
            // delete가 insert 보다 앞이면 insert 위치 당겨줌
            if (op1.getPosition() < op2.getPosition()) op2.setPosition(op2.getPosition() - 1);
            else op1.setPosition(op1.getPosition() + 1);    // 반대면 delete 위치를 뒤로 조정

        }
        // 4. 둘 다 delete인 경우
        else if (op1.getOperationType() == OperationType.DELETE && op2.getOperationType() == OperationType.DELETE) {
            // op1이 앞이면 op2의 위치를 한 칸 당겨줌
            if (op1.getPosition() < op2.getPosition()) op2.setPosition(op2.getPosition() - 1);
            // op2가 앞이면 op1의 위치를 한 칸 당여줌
            else if (op1.getPosition() > op2.getPosition()) op1.setPosition(op1.getPosition() - 1);
            // 같은 위치 삭제 시 둘 중 하나 무시
            else op2.setPosition(-1);
        }

        // 조정된 두 연산을 배열로 변환
        return new Operation[]{op1, op2};
    }

    // 여러 선행 연산에 대해 순차적으로 조정
    // op: 내가 실행하려는 연산
    // priorOps: 이미 실행된 연산
    public Operation transformAgainstAll(Operation op, List<Operation> priorOps) {
        // op를 복사하여 원본 유지
        Operation result = copy(op);

        // 각 우선 순위에 대해 해결
        for (Operation prior : priorOps) {
            // prior 연산과 result 사이의 충돌 해결 후,
            // result 업데이트
            result = transform(prior, result)[1];
        }
        // 모든 prior이 반영된 result 반환
        return result;
    }

    // 문자열에 연산 적용
    public String apply(String original, Operation op) {
        if (op.getOperationType() == OperationType.INSERT) {
            // insert: 지정 위치에 문자열(content) 삽입
            return original.substring(0, op.getPosition()) + op.getContent() + original.substring(op.getPosition());
        } else if (op.getOperationType() == OperationType.DELETE) {
            // delete: 지정 위치의 문자 하나 제거
            return original.substring(0, op.getPosition()) + original.substring(op.getPosition() + 1);
        }
        // insert/delete 외에는 무시
        return original;
    }

    // Operation 복사
    private Operation copy(Operation op) {
        return new Operation(
                op.getOperationType(),
                op.getDocumentId(),
                op.getPosition(),
                op.getContent()
        );
    }
}
