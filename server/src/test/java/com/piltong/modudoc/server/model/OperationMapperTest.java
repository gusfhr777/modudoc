package com.piltong.modudoc.server.model;

import com.piltong.modudoc.common.model.OperationDto;
import com.piltong.modudoc.common.model.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class OperationMapperTest {
    List<Operation> opList;
    @BeforeEach
    void setUp() {
        Operation op1 = new Operation(OperationType.INSERT, 1, 3, "as");
        Operation op2 = new Operation(OperationType.INSERT, 1, 3, "as");
        opList = new ArrayList<>();
        opList.add(op1);
        opList.add(op2);
    }

    @Test
    void toDto() {
    }

    @Test
    void toEntity() {
    }

    @Test
    void testToEntity() {
    }

    @Test
    void testToDtoList() {

        System.out.println(opList);
        List<OperationDto> dtoList = OperationMapper.toDto(opList);
        System.out.println(dtoList);
        assertEquals(opList.size(), dtoList.size());
        assertEquals(opList.get(0).getDocId(), dtoList.get(0).getDocId());
        assertEquals(opList.get(0).getContent(), dtoList.get(0).getContent());
        assertEquals(opList.get(0).getOperationType(), dtoList.get(0).getOperationType());
        assertEquals(opList.get(0).getPosition(), dtoList.get(0).getPosition());
    }
}