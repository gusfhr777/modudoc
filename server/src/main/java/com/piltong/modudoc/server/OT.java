package com.piltong.modudoc.server;

import java.util.*;

/* 추후 확장 기능
 * Undo 기능 (history가 필요함)
 * 문자열 전체 변경 기록 저장 (history)
 */

// 동시 편집 충돌 해결 알고리즘
public class OT {

    // 단일 삽입 or 삭제 연산
    static class Operation {
        String type;    // "insert" or "delete"
        int position;   // 수정 위치
        String ch;      // insert 시 삽입할 문자

        public Operation(String type, int position, String ch) {
            this.type = type;
            this.position = position;
            this.ch = ch;
        }

        public Operation(String type, int position) {
            this(type, position, null);
        }

        public Operation copt() {
            return new Operation(type, position, ch);
        }

        @Override
        public String toString() {
            return  type.equals("insert")
                    ? "insert(" + position + ", '" + ch + "')"
                    : "delete(" + position + ")";
        }
    }

    // 외부에서 주어지는 operation 문자열을 Operation 객체로 변환
    private Operation parseOperation(String operation) {
        String[] parts = operation.trim().split(" ");
        if (parts.length < 2) return null;

        String type = parts[0];
        int position = Integer.parseInt(parts[1]);

        if (type.equals("insert") && parts.length == 3) {
            return new Operation(type, position, parts[2]);
        } else if (type.equals("delete")) {
            return new Operation(type, position);
        } else {
            return null;
        }
    }

    // 단일 연산을 문자열에 적용
    public String transform(String original, String operation) {
        Operation op = parseOperation(operation);
        if (op == null) return original;

        if (op.type.equals("insert")) {
            if (op.position < 0 || op.position > original.length()) return original;
            return original.substring(0, op.position) + op.ch + original.substring(op.position);
        } else if (op.type.equals("delete")) {
            if (op.position < 0 || op.position >= original.length()) return original;
            return original.substring(0, op.position) + original.substring(op.position + 1);
        }

        return original;
    }

    // 두 연산 간 충돌 해결
    public Operation[] transform(Operation op1, Operation op2) {
        op1 = op1.copy();
        op2 = op2.copy();

        if (op1.type.equals("insert") && op2.type.equals("insert")) {
            if (op1.position <= op2.position) op2.position++;
            else op1.position++;
        } else if (op1.type.equals("insert") && op2.type.equals("delete")) {
            if (op1.position <= op2.position) op2.position++;
            else op1.position--;
        } else if (op1.type.equals("delete") && op2.type.equals("insert")) {
            if (op1.position < op2.position) op2.position--;
            else op1.position++;
        } else if (op1.type.equals("delete") && op2.type.equals("delete")) {
            if (op1.position < op2.position) op2.position--;
            else if (op1.position > op2.position) op1.position--;
            else op2.position = -1; // 같은 위치 삭제 (하나의 입력 무시)
        }
        return new Operation[] {op1, op2};
    }

    // 여러 선행 연산에 대해 순차적으로 조정
    public Operation transformAgainstAll(Operation op, List<Operation> priorOps) {
        Operation result = op.copy();
        for (Operation prior : priorOps) {
            result = transform(prior, result)[1];
        }
        return result;
    }
}
