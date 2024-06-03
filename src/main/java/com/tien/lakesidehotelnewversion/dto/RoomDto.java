package com.tien.lakesidehotelnewversion.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
public class RoomDto {
    private String roomType;
    private BigDecimal roomPrice;
    private MultipartFile photoUpload;

    public RoomDto(String roomType, BigDecimal roomPrice, MultipartFile photoUpload) {
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.photoUpload = photoUpload;
    }
}

