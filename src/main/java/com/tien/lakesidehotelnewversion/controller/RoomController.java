package com.tien.lakesidehotelnewversion.controller;

import com.tien.lakesidehotelnewversion.dto.RoomDto;
import com.tien.lakesidehotelnewversion.exception.ResourceNotFoundException;
import com.tien.lakesidehotelnewversion.response.RoomResponseDto;
import com.tien.lakesidehotelnewversion.response.RoomResponseDtoNoPhoto;
import com.tien.lakesidehotelnewversion.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:5173/")
@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    @PostMapping("/add/new-room")
    public ResponseEntity<RoomResponseDtoNoPhoto> addNewRoom(
            @RequestParam String roomType,
            @RequestParam BigDecimal roomPrice,
            @RequestParam MultipartFile photoUpload
    ) throws SQLException, IOException {
        RoomDto roomDto = new RoomDto(roomType,roomPrice,photoUpload);
        var response = roomService.addNewRoom(roomDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/room/types")
    public List<String> getRoomTypes(){
        return roomService.getAllRoomTypes();
    }


    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomResponseDto>> getAllRooms() throws SQLException{
        return roomService.getAllRooms();
    }

    @DeleteMapping("/delete/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable("roomId") Long roomId
    ){
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{roomId}")
    public ResponseEntity<RoomResponseDto> updateRoom(
            @PathVariable("roomId") Long roomId,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) BigDecimal roomPrice,
            @RequestParam(required = false) MultipartFile photo
    ) throws SQLException, IOException {
        RoomResponseDto updatedRoom = roomService.updateRoom(roomId, roomType, roomPrice, photo);
        return ResponseEntity.ok(updatedRoom);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getRoomById(@PathVariable("roomId") Long roomId) {

//        Optional<RoomResponseDto> room = roomService.getRoomById(roomId);
            try{
                RoomResponseDto roomResponse = roomService.getRoomById(roomId);
                return ResponseEntity.ok(roomResponse);
            }catch (ResourceNotFoundException ex){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            }

//        if (room.isPresent()) {
//            return ResponseEntity.ok(room);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
    }

    @GetMapping("/available-rooms")
    public ResponseEntity<List<RoomResponseDto>> getAvailableRooms(
            @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam("roomType") String roomType
            ) throws SQLException {
        List<RoomResponseDto> availableRoomsResponses = roomService.getAvailableRooms(checkInDate, checkOutDate, roomType);
        if(availableRoomsResponses.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(availableRoomsResponses);
        }
    }

}
