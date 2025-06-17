package com.piltong.modudoc.server.network;

import com.piltong.modudoc.common.document.*;
import com.piltong.modudoc.common.network.*;
import com.piltong.modudoc.common.operation.*;
import com.piltong.modudoc.server.service.DocumentService;
import com.piltong.modudoc.server.service.SyncService;

import java.time.LocalDateTime;
import java.util.List;

// 기초 틀
public class ServerNetworkListenerImpl implements ServerNetworkListener {
    private final DocumentService documentService;
    private final SyncService syncService;

    public ServerNetworkListenerImpl(DocumentService documentService, SyncService syncService) {
        this.documentService = documentService;
        this.syncService = syncService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, R> R onCommandReceived(ClientCommand command, T payload) throws CommandException {
        switch (command) {
            case CREATE_DOCUMENT: {
                DocumentDto dto = (DocumentDto) payload;
                documentService.updateDocument(dto.getId(), dto.getTitle(), dto.getContent());
                return (R) new DocumentSummary(
                        dto.getId(),
                        dto.getTitle(),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of()
                );
            }

            case READ_DOCUMENT:
                return (R) documentService.getDocument((String) payload);

            case UPDATE_DOCUMENT: {
                DocumentDto dto = (DocumentDto) payload;
                documentService.updateDocument(dto.getId(), dto.getTitle(), dto.getContent());
                return (R) new DocumentSummary(
                        dto.getId(),
                        dto.getTitle(),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        List.of()
                );
            }

            case DELETE_DOCUMENT:
                documentService.removeDocument((String) payload);
                return (R) payload;

            case PROPAGATE_OPERATION:
                OperationDto opDto = (OperationDto) payload;
                syncService.syncUpdate(opDto.getDocumentId(), opDto, "null");
                return null;

            default:
                throw new CommandException("알 수 없는 커맨드: " + command);
        }
    }

    @Override
    public void onNetworkError(Throwable t) {
        System.err.println("네트워크 오류 발생: " + t.getMessage());
        t.printStackTrace();
    }
}
