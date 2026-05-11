package com.example.worker_observer_system.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetaDataDto {
    private long totalItems;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    public static class MetaDataDtoBuilder{
        public MetaDataDtoBuilder paginationDto(PaginationDto paginationDto) {
            if(paginationDto == null) return this;

            this.currentPage = paginationDto.getPage();
            this.pageSize = paginationDto.getLimit();
            return this;
        }
    }
}
