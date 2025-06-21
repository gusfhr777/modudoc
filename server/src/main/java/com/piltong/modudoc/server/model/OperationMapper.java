package com.piltong.modudoc.server.model;

import com.piltong.modudoc.common.model.OperationDto;

import java.util.List;
import java.util.stream.Collectors;

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


    // DTO 리스트 -> 문서 리스트 변환
    public static List<Operation> toEntity(List<OperationDto> dtoList) {
        return dtoList
                .stream()
                .map(OperationMapper::toEntity)
                .collect(Collectors.toList());
    }


    // docList -> dtoList로 변환
    public static List<OperationDto> toDto(List<Operation> operationList) {
        return operationList
                .stream()
                .map(OperationMapper::toDto)
                .collect(Collectors.toList());

    }
}
