package com.tien.lakesidehotelnewversion.mapper;


import com.tien.lakesidehotelnewversion.dto.RoomDto;
import com.tien.lakesidehotelnewversion.entity.Room;
import com.tien.lakesidehotelnewversion.exception.PhotoRetrievalException;
import com.tien.lakesidehotelnewversion.response.RoomResponseDto;
import com.tien.lakesidehotelnewversion.response.RoomResponseDtoNoPhoto;
import com.tien.lakesidehotelnewversion.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomMapper {

    // Room response
    public RoomResponseDto toRoomResponseDto(Room room){
        byte[] photoBytes = null;
        Blob photoBlob = room.getPhoto();
        if (photoBlob != null){
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch(SQLException e){
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
        return new RoomResponseDto(room.getId(), room.getRoomType(), room.getRoomPrice(), room.isBooked(), photoBytes);
    }

    // Room response without photo
    public RoomResponseDtoNoPhoto toRoomResponseDtoWithOutPhoto(Room room){
        return new RoomResponseDtoNoPhoto(room.getId(), room.getRoomType(), room.getRoomPrice(), room.isBooked());
    }

    // List rooms to list room response
//    public List<RoomResponseDto> toRoomResponseDtoList(List<Room> rooms) throws SQLException{
//        List<RoomResponseDto> roomResponseDtoList = new ArrayList<>();
//        for(Room room : rooms){
//            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
//            if(photoBytes != null && photoBytes.length >0){
//                String base64Photo = Base64.encodeBase64String(photoBytes);
//                RoomResponseDto roomResponseDto =
//            }
//        }
//    }
}