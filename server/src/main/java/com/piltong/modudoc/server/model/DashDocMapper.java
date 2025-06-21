package com.piltong.modudoc.server.model;

import com.piltong.modudoc.common.model.DashDocDto;

import java.util.List;
import java.util.stream.Collectors;

public class DashDocMapper {
    public static DashDocDto toDto(DashDoc dashDoc) {
        return new DashDocDto(dashDoc.getId(),
                dashDoc.getTitle(),
                dashDoc.getCreatedDate(),
                dashDoc.getModifiedDate(),
                dashDoc.getActiveUserCount());
    }

    public static DashDoc toEntity(DashDocDto docDto) {
        return new DashDoc(docDto.getId(), docDto.getTitle(), docDto.getCreatedDate(), docDto.getModifiedDate(), docDto.getActiveUserCount());
    }



    // DTO 리스트 -> 문서 리스트 변환
    public static List<DashDoc> toEntity(List<DashDocDto> dtoList) {
        return dtoList
                .stream()
                .map(DashDocMapper::toEntity)
                .collect(Collectors.toList());
    }

}