package com.tien.lakesidehotelnewversion.service;

import com.tien.lakesidehotelnewversion.dto.BookingDto;
import com.tien.lakesidehotelnewversion.response.BookingResponseDto;

import java.util.List;

public interface BookingService {
    List<BookingResponseDto> getAllBookings();

    BookingResponseDto findByBookingConfirmationCode(String confirmationCode);

    List<BookingResponseDto> getBookingsByUserEmail(String email);

    String saveBooking(Long roomId, BookingDto bookingRequest);

    void cancelBooking(Long bookingId);
}
