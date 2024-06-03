package com.tien.lakesidehotelnewversion.service;

import com.tien.lakesidehotelnewversion.dto.RoomDto;
import com.tien.lakesidehotelnewversion.entity.Room;
import com.tien.lakesidehotelnewversion.exception.InternalServerException;
import com.tien.lakesidehotelnewversion.exception.ResourceNotFoundException;
import com.tien.lakesidehotelnewversion.mapper.RoomMapper;
import com.tien.lakesidehotelnewversion.repository.RoomRepository;
import com.tien.lakesidehotelnewversion.response.RoomResponseDto;
import com.tien.lakesidehotelnewversion.response.RoomResponseDtoNoPhoto;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    @Override
    public RoomResponseDtoNoPhoto addNewRoom(RoomDto roomDto) throws SQLException, IOException {
        Room room = new Room();
        room.setRoomType(roomDto.getRoomType());
        room.setRoomPrice(roomDto.getRoomPrice());
        if(!roomDto.getPhotoUpload().isEmpty()){
            byte[] photoBytes = roomDto.getPhotoUpload().getBytes();
            Blob photoBLob = new SerialBlob(photoBytes);
            room.setPhoto(photoBLob);
        }
        var savedRoom = roomRepository.save(room);

        return roomMapper.toRoomResponseDtoWithOutPhoto(savedRoom);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public ResponseEntity<List<RoomResponseDto>> getAllRooms() throws SQLException{
        List<Room> rooms = roomRepository.findAll();
        List<RoomResponseDto> roomResponseDtoList = new ArrayList<>();
        for(Room room : rooms){
            byte[] photoBytes = getRoomPhotoByRoomId(room.getId());
            if(photoBytes != null && photoBytes.length >0){
                String base64Photo = Base64.encodeBase64String(photoBytes);
                RoomResponseDto roomResponseDto = roomMapper.toRoomResponseDto(room);
                roomResponseDto.setPhoto(base64Photo);
                roomResponseDtoList.add(roomResponseDto);
            }
        }
        return ResponseEntity.ok(roomResponseDtoList);
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long id) throws SQLException{
        Optional<Room> theRoom = roomRepository.findById(id);
        if(theRoom.isEmpty()){
            throw new ResourceNotFoundException("Sorry, Room not found!");
        }
        Blob photoBlob = theRoom.get().getPhoto();
        if(photoBlob != null){
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null;
    }

    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        if(room.isPresent()) {
            roomRepository.deleteById(roomId);
        }
    }

    @Override
    public RoomResponseDto updateRoom(Long roomId, String roomType, BigDecimal roomPrice, MultipartFile photo) throws IOException, SQLException {
        byte[] photoBytes = photo != null && !photo.isEmpty() ?
                photo.getBytes() : getRoomPhotoByRoomId(roomId);
        Blob photoBlob = photoBytes != null && photoBytes.length > 0 ?
                new SerialBlob(photoBytes) : null;
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        if (roomType != null){
            room.setRoomType(roomType);
        }
        if( roomPrice != null ){
            room.setRoomPrice(roomPrice);
        }
        if( photoBytes != null && photoBytes.length > 0){
            try{
                room.setPhoto(new SerialBlob(photoBytes));
            } catch (SQLException ex){
                throw new InternalServerException("Error updating room");
            }
        }
        return roomMapper.toRoomResponseDto(room);
    }

    @Override
    public RoomResponseDto getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("No room found with ID: " + roomId));
        return roomMapper.toRoomResponseDto(room);
//
//        try{
//            Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
//
//            return roomMapper.toRoomResponseDto(room);
//        } catch (NullPointerException e){
//            throw new ResourceNotFoundException("No room found with ID: " + roomId);
//        }

//        Room room = roomRepository.findById(roomId)
//                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
//        return Optional.of(roomMapper.toRoomResponseDto(room));
    }

    @Override
    public List<RoomResponseDto> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) throws SQLException {
        List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndType(checkInDate,checkOutDate,roomType);
        List<RoomResponseDto> availableRoomsResponses = new ArrayList<>();
        for(Room room : availableRooms) {
            byte[] photoBytes = getRoomPhotoByRoomId(room.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String photoBase64 = Base64.encodeBase64String(photoBytes);
                RoomResponseDto roomResponse = roomMapper.toRoomResponseDto(room);
                roomResponse.setPhoto(photoBase64);
                availableRoomsResponses.add(roomResponse);
            }
        }
        return availableRoomsResponses;
    }


}
