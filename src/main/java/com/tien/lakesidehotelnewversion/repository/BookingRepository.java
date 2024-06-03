package com.tien.lakesidehotelnewversion.repository;

import com.tien.lakesidehotelnewversion.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findByBookingConfirmationCode(String confirmationCode);

    List<Booking> findByGuestEmail(String email);
}
