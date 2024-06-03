package com.tien.lakesidehotelnewversion.mapper;

import com.tien.lakesidehotelnewversion.dto.BookingDto;
import com.tien.lakesidehotelnewversion.entity.Booking;
import com.tien.lakesidehotelnewversion.entity.Room;
import com.tien.lakesidehotelnewversion.response.BookingResponseDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookingMapper {
    public Booking toBooking(BookingDto bookingDto, Room room){
        Booking booking = new Booking();
        booking.setCheckInDate(bookingDto.getCheckInDate());
        booking.setCheckOutDate(bookingDto.getCheckOutDate());
        booking.setGuestFullName(booking.getGuestFullName());
        booking.setGuestEmail(booking.getGuestEmail());
        booking.setNumOfAdults(booking.getNumOfAdults());
        booking.setNumOfChildren(booking.getNumOfChildren());
        booking.setTotalNumberOfGuest(booking.getTotalNumberOfGuest());
        booking.setRoom(room);
        return booking;
    }

    public BookingResponseDto toBookingRoomResponseDto(Booking booking, Room room){
        return new BookingResponseDto(
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getGuestFullName(),
                booking.getGuestEmail(),
                booking.getNumOfAdults(),
                booking.getNumOfChildren(),
                booking.getTotalNumberOfGuest(),
                booking.getBookingConfirmationCode(),
                room
        );
    }


}
