package com.piltong.modudoc.server.core;

import com.piltong.modudoc.common.operation.Operation;
import com.piltong.modudoc.common.operation.OperationType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// OT를 테스트하는 코드
// 실행 방법: 각 메서드 왼쪽에 ▶ 버튼 클릭
public class OTTest {

    private final OT ot = new OT();

    @Test
    void testInsertInsertDifferentPosition() {
        Operation a = new Operation(OperationType.INSERT, "doc1", 2, "A");
        Operation b = new Operation(OperationType.INSERT, "doc1", 5, "B");

        Operation[] transformed = ot.transform(a, b);

        assertEquals(2, transformed[0].getPosition());
        assertEquals(6, transformed[1].getPosition());
    }

    @Test
    void testInsertInsertSamePosition() {
        Operation a = new Operation(OperationType.INSERT, "doc1", 3, "A");
        Operation b = new Operation(OperationType.INSERT, "doc1", 3, "B");

        Operation[] transformed = ot.transform(a, b);

        assertEquals(3, transformed[0].getPosition());
        assertEquals(4, transformed[1].getPosition());
    }

    @Test
    void testInsertDelete() {
        Operation insert = new Operation(OperationType.INSERT, "doc1", 2, "XY");
        Operation delete = new Operation(OperationType.DELETE, "doc1", 4, "Z");

        Operation[] transformed = ot.transform(insert, delete);

        assertEquals(2, transformed[0].getPosition());
        assertEquals(6, transformed[1].getPosition());
    }

    @Test
    void testDeleteInsert() {
        Operation delete = new Operation(OperationType.DELETE, "doc1", 1, "A");
        Operation insert = new Operation(OperationType.INSERT, "doc1", 3, "BC");

        Operation[] transformed = ot.transform(delete, insert);

        assertEquals(1, transformed[0].getPosition());
        assertEquals(1, transformed[1].getPosition());
    }

    @Test
    void testDeleteDeleteSamePosition() {
        Operation a = new Operation(OperationType.DELETE, "doc1", 2, "X");
        Operation b = new Operation(OperationType.DELETE, "doc1", 2, "Y");

        Operation[] transformed = ot.transform(a, b);

        assertEquals(2, transformed[0].getPosition());
        assertEquals(-1, transformed[1].getPosition());
    }

    @Test
    void testTransformAgainstAll() {
        List<Operation> history = new ArrayList<>();
        history.add(new Operation(OperationType.INSERT, "doc1", 0, "A"));
        history.add(new Operation(OperationType.INSERT, "doc1", 1, "B"));

        Operation current = new Operation(OperationType.INSERT, "doc1", 2, "C");

        Operation transformed = ot.transformAgainstAll(current, history);

        assertEquals(4, transformed.getPosition());
    }

    @Test
    void testApplyInsert() {
        Operation op = new Operation(OperationType.INSERT, "doc1", 2, "XY");
        String result = ot.apply("hello", op);
        assertEquals("heXYllo", result);
    }

    @Test
    void testApplyDelete() {
        Operation op = new Operation(OperationType.DELETE, "doc1", 1, "el");
        String result = ot.apply("hello", op);
        assertEquals("hlo", result);
    }
}
