package com.tien.lakesidehotelnewversion.service;

import com.tien.lakesidehotelnewversion.dto.RoomDto;
import com.tien.lakesidehotelnewversion.entity.Room;
import com.tien.lakesidehotelnewversion.response.RoomResponseDto;
import com.tien.lakesidehotelnewversion.response.RoomResponseDtoNoPhoto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomService {
    RoomResponseDtoNoPhoto addNewRoom(RoomDto roomDto) throws SQLException, IOException;

    public List<String> getAllRoomTypes();


    ResponseEntity<List<RoomResponseDto>> getAllRooms() throws SQLException;

    byte[] getRoomPhotoByRoomId(Long id) throws SQLException;

    void deleteRoom(Long roomId);

    RoomResponseDto updateRoom(Long roomId, String roomType, BigDecimal roomPrice, MultipartFile photo) throws IOException, SQLException;

    RoomResponseDto getRoomById(Long roomId);

    List<RoomResponseDto> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) throws SQLException;
}
