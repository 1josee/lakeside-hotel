package com.tien.lakesidehotelnewversion.controller;

import com.tien.lakesidehotelnewversion.dto.BookingDto;
import com.tien.lakesidehotelnewversion.exception.InvalidBookingRequestException;
import com.tien.lakesidehotelnewversion.exception.ResourceNotFoundException;
import com.tien.lakesidehotelnewversion.response.BookingResponseDto;
import com.tien.lakesidehotelnewversion.service.BookingService;
import com.tien.lakesidehotelnewversion.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.Port;
import java.util.List;

@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final RoomService roomService;

    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponseDto>> getAllBookings(){
        List<BookingResponseDto> allBookingsResponses = bookingService.getAllBookings();
        return ResponseEntity.ok(allBookingsResponses);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(
            @PathVariable String confirmationCode
    ){
        try{
            BookingResponseDto bookingResponse = bookingService.findByBookingConfirmationCode(confirmationCode);
            return ResponseEntity.ok(bookingResponse);
        } catch (ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/user/{email}/bookings")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByUserEmail(
            @PathVariable String email
    ){
        List<BookingResponseDto> userBookings = bookingService.getBookingsByUserEmail(email);
        return ResponseEntity.ok(userBookings);
    }

    @PostMapping("room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(
            @PathVariable Long roomId,
            @RequestBody BookingDto bookingRequest
            ){
        try{
            String confirmationCode = bookingService.saveBooking(roomId, bookingRequest);
            return ResponseEntity.ok("Room book successfully, Your booking confirmation code is :" + confirmationCode);
        } catch (InvalidBookingRequestException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(
            @PathVariable Long bookingId
    ){
        bookingService.cancelBooking(bookingId);
    }
}
