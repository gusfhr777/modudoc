package com.piltong.modudoc.server.core;

import java.util.*;

/** 아직 불완전한 코드
 * 추후 확장 기능
 * Undo 기능 (history가 필요함)
 * 문자열 전체 변경 기록 저장 (history)
 */

// 동시 편집 충돌 해결 알고리즘
public class OT {

    // 단일 삽입 or 삭제 연산
    static class Operation {
        String type;    // "insert" or "delete"
        int position;   // 수정 위치
        String content;      // insert 시 삽입할 문자

        // 생성자: 초기화
        public Operation(String type, int position, String content) {
            this.type = type;
            this.position = position;
            this.content = content;
        }

        // 생성자: 초기화
        public Operation(String type, int position) {
            this(type, position, null);
        }

        // 새로운 객체를 생성하여 복사
        public Operation copy() {
            return new Operation(type, position, content);
        }

        // 객체의 타입을 반환
        @Override
        public String toString() {
            // insert면 수정 위치와 삽입할 문자 반환.
            // delete면 수정 위치 반환.
            return  type.equals("insert")
                    ? "insert(" + position + ", '" + content + "')"
                    : "delete(" + position + ")";
        }
    }

    // 외부에서 주어지는 operation 문자열을 Operation 객체로 변환
    private Operation parseOperation(String operation) {
        // 문자열을 공백 기준으로 나눔
        String[] parts = operation.trim().split(" ");
        if (parts.length < 2) return null;

        String type = parts[0]; // 연산 타입: insert 또는 delete
        int position = Integer.parseInt(parts[1]);  // 문자열로 받은 위치를 정수형으로 변환

        // insert 연산. 삽입할 문자가 있으면 해당 위치에 삽입
        if (type.equals("insert") && parts.length == 3) {
            return new Operation(type, position, parts[2]);
        }
        // delete 연산. 해당 위치 문자 삭제
        else if (type.equals("delete")) {
            return new Operation(type, position);
        }
        // 조건 외는 null 반환
        else {
            return null;
        }
    }

    // 주어진 문자열(original)에 연산 문자열(operation) 적용값 반환
    public String transform(String original, String operation) {
        // 연산 문자열을 Operation 객체로 바꿈
        Operation op = parseOperation(operation);
        // 바꾸는게 실패하면 original 반환
        if (op == null) return original;

        // 삽입 연산 처리
        if (op.type.equals("insert")) {
            // 삽입 위치가 잘못되었으면 original 반환
            if (op.position < 0 || op.position > original.length()) return original;
            // 문자열을 삽입 위치 기준으로 분리후 사이에 문자를 끼워넣어 반환
            return original.substring(0, op.position) + op.content + original.substring(op.position);
        }
        // 삭제 연산 처리
        else if (op.type.equals("delete")) {
            // 삭제 위치가 잘못되었으면 original 반환
            if (op.position < 0 || op.position >= original.length()) return original;
            // 해당 위치의 문자를 제거한 문자열 반환
            return original.substring(0, op.position) + original.substring(op.position + 1);
        }

        // insert/delete 외의 경우 original 반환
        return original;
    }

    // 두 연산 간 충돌을 해결하여 위치 재조정 결과 반환
    public Operation[] transform(Operation op1, Operation op2) {
        // 두 연산 모두 복사하여 원본 유지
        op1 = op1.copy();
        op2 = op2.copy();

        // 1. 둘 다 insert인 경우
        if (op1.type.equals("insert") && op2.type.equals("insert")) {
            // op1이 먼저 왔거나 같은 위치면, op2의 위치를 한 칸 밀어줌
            if (op1.position <= op2.position) op2.position++;
            else op1.position++;    // 반대면 op1을 한 칸 밀어줌
        }
        // 2. insert, delete인 경우
        else if (op1.type.equals("insert") && op2.type.equals("delete")) {
            // insert가 delete 보다 앞이면 delete 위치 뒤로 조정
            if (op1.position <= op2.position) op2.position++;
            else op1.position--;    // 반대면 insert 위치를 한 칸 당겨줌
        }
        // 3. delete, insert인 경우
        else if (op1.type.equals("delete") && op2.type.equals("insert")) {
            // delete가 insert 보다 앞이면 insert 위치 당겨줌
            if (op1.position < op2.position) op2.position--;
            else op1.position++;    // 반대면 delete 위치를 뒤로 조정
        }
        // 4. 둘 다 delete인 경우
        else if (op1.type.equals("delete") && op2.type.equals("delete")) {
            // op1이 앞이면 op2의 위치를 한 칸 당겨줌
            if (op1.position < op2.position) op2.position--;
            // op2가 앞이면 op1의 위치를 한 칸 당겨줌
            else if (op1.position > op2.position) op1.position--;
            // 같은 위치 삭제 시 둘 중 하나 무시
            else op2.position = -1;
        }

        // 조정된 두 연산을 배열로 반환
        return new Operation[] {op1, op2};
    }

    // 여러 선행 연산에 대해 순차적으로 조정
    // op: 내가 실행하여는 연산.
    // priorOps: 이미 실행된 연산.
    public Operation transformAgainstAll(Operation op, List<Operation> priorOps) {
        // Operation을 복사해서 원본 유지
        Operation result = op.copy();

        // 각 우선 순위에 대해 해결
        for (Operation prior : priorOps) {
            // prior 연산과 result 사이의 충돌 해결 후,
            // result 업데이트
            result = transform(prior, result)[1];
        }
        // 모든 prior이 반영된 result 반환
        return result;
    }
}
