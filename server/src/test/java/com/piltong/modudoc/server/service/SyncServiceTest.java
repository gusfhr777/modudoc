package com.piltong.modudoc.server.service;

import com.piltong.modudoc.common.document.Document;
import com.piltong.modudoc.common.operation.Operation;
import com.piltong.modudoc.common.operation.OperationDto;
import com.piltong.modudoc.common.operation.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SyncServiceTest {

    private SyncService syncService;

    @BeforeEach
    void setUp() {
        syncService = new SyncService();
    }

    @Test
    void testSyncInsertOperation() {
        // 초기 문서 등록
        syncService.updateDocument("doc1", "title1", "hello");

        // 클라이언트가 '!' 삽입
        OperationDto dto = new OperationDto(OperationType.INSERT, "doc1", 5, "!");
        syncService.syncUpdate("doc1", dto, "user1");

        Document result = syncService.getDocument("doc1");
        assertEquals("hello!", result.getContent());
    }

    @Test
    void testSyncDeleteOperation() {
        syncService.updateDocument("doc2", "title2", "hello!");

        // 마지막 문자 삭제
        OperationDto dto = new OperationDto(OperationType.DELETE, "doc2", 5, null);
        syncService.syncUpdate("doc2", dto, "user1");

        Document result = syncService.getDocument("doc2");
        assertEquals("hello", result.getContent());
    }

    @Test
    void testMultipleSequentialOperations() {
        syncService.updateDocument("doc3", "title3", "cat");

        syncService.syncUpdate("doc3", new OperationDto(OperationType.INSERT, "doc3", 3, "s"), "u1");
        syncService.syncUpdate("doc3", new OperationDto(OperationType.INSERT, "doc3", 4, "!"), "u1");

        Document result = syncService.getDocument("doc3");
        assertEquals("cats!", result.getContent());
    }
}
