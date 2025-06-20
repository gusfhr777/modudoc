package com.piltong.modudoc.server.model;

import com.piltong.modudoc.common.model.OperationDto;

public class OperationMapper {


    // DTO 객체로 변환하는 함수
    // 추후 최적화 필요
    public static OperationDto toDto(Operation operation) {

        return new OperationDto(
                operation.getOperationType(),
                operation.getDocId(),
                operation.getPosition(),
                operation.getContent()
        );
    }

    // 엔티티 객체로 변환하는 함수
    // 추후 최적화 필요
    public static Operation toEntity(OperationDto dto) {

        return new Operation(
                dto.getOperationType(),
                dto.getDocId(),
                dto.getPosition(),
                dto.getContent()
        );
    }
}
