package com.tien.lakesidehotelnewversion.service;

import com.tien.lakesidehotelnewversion.dto.BookingDto;
import com.tien.lakesidehotelnewversion.entity.Booking;
import com.tien.lakesidehotelnewversion.entity.Room;
import com.tien.lakesidehotelnewversion.exception.InvalidBookingRequestException;
import com.tien.lakesidehotelnewversion.exception.ResourceNotFoundException;
import com.tien.lakesidehotelnewversion.mapper.BookingMapper;
import com.tien.lakesidehotelnewversion.repository.BookingRepository;
import com.tien.lakesidehotelnewversion.repository.RoomRepository;
import com.tien.lakesidehotelnewversion.response.BookingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final RoomRepository roomRepository;
    @Override
    public List<BookingResponseDto> getAllBookings() {
        List<Booking> allBookings = bookingRepository.findAll();
        List<BookingResponseDto> allBookingsResponses = new ArrayList<>();
        for(Booking booking : allBookings){
            Room room = roomRepository.findById(booking.getRoom().getId()).get();
            BookingResponseDto bookingResponse = bookingMapper.toBookingRoomResponseDto(booking,room);
            allBookingsResponses.add(bookingResponse);
        }
        return allBookingsResponses;
    }

    @Override
    public BookingResponseDto findByBookingConfirmationCode(String confirmationCode) {
        try{
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode);
            Room room = roomRepository.findById(booking.getRoom().getId()).get();
            BookingResponseDto bookingResponse = bookingMapper.toBookingRoomResponseDto(booking, room);
            return bookingResponse;
        } catch (NullPointerException e){
            throw new ResourceNotFoundException("No booking found with booking code: " + confirmationCode);
        }
    }

    @Override
    public List<BookingResponseDto> getBookingsByUserEmail(String email) {
        List<BookingResponseDto> userBookings = bookingRepository.findByGuestEmail(email)
                .stream()
                .map(booking -> {
                    Room room = roomRepository.findById(booking.getRoom().getId()).get();
                    return bookingMapper.toBookingRoomResponseDto(booking, room);
                })
                .collect(Collectors.toList());
        return userBookings;
    }

    @Override
    public String saveBooking(Long roomId, BookingDto bookingRequest) {
        if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
            throw new InvalidBookingRequestException("Check-in date must come before check-out date");
        }
        Room room = roomRepository.findById(roomId).get();
        Booking booking = bookingMapper.toBooking(bookingRequest, room);
        List<Booking> existingBookings = room.getBookings();
        Boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBookings);
        if(roomIsAvailable){
            room.addBooking(booking);
            bookingRepository.save(booking);
        } else {
            throw new InvalidBookingRequestException("Sorry, This room is not available for the selected dates");
        }
        return booking.getBookingConfirmationCode();
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }


    private boolean roomIsAvailable(BookingDto bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }

//    public List<StudentResponseDto> findStudentByName(
//            String name
//    ){
//        return repository.findAllByFirstnameContaining(name)
//                .stream()
//                .map(studentMapper:: toStudentResponseDto)
//                .collect(Collectors.toList());
//    }
}
