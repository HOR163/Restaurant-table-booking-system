package ee.hor.tablebooking.service;

import ee.hor.tablebooking.dto.BookingDto;
import ee.hor.tablebooking.entity.BookingEntity;
import ee.hor.tablebooking.entity.TableEntity;
import ee.hor.tablebooking.mapper.BookingMapper;
import ee.hor.tablebooking.repository.BookingRepository;
import ee.hor.tablebooking.repository.TableRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private static final int BOOKING_PENDING_MINUTES = 15;

    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final TableRepository tableRepository;

    public BookingDto getBooking(UUID id) {
        // FIXME: throw an error if not found
        BookingEntity bookingEntity = bookingRepository.findById(id).orElse(null);

        return bookingMapper.mapToDto(bookingEntity);
    }

    public BookingDto addBooking(BookingDto bookingDto) {
        BookingEntity bookingEntity = bookingMapper.mapToEntity(bookingDto);

        // Manually add the whole table entity to the booking proxy, so that mapStruct can later use it for converting
        // to DTO
        TableEntity table = tableRepository.findById(bookingDto.getTableId())
                .orElseThrow(() -> new EntityNotFoundException("Table not found"));

        bookingEntity.setTable(table);
        bookingEntity.setPending(true);
        bookingEntity.setPendingEndTime(OffsetDateTime.now().plusMinutes(BOOKING_PENDING_MINUTES));

        BookingEntity newBooking = bookingRepository.save(bookingEntity);

        return bookingMapper.mapToDto(newBooking);
    }

    public void deleteBooking(UUID id) {
        bookingRepository.deleteById(id);
    }

    public List<BookingDto> getUserBookings(UUID userId, OffsetDateTime startTime, OffsetDateTime endTime) {
        List<BookingEntity> bookings = bookingRepository.findAllByUserIdAndStartTimeBetween(userId, startTime, endTime);
        return bookingMapper.mapToDto(bookings);
    }

    public List<BookingDto> getRestaurantBookings(UUID restaurantId, OffsetDateTime spanStart, OffsetDateTime spanEnd) {
        List<BookingEntity> bookings;
        if (spanStart != null && spanEnd != null) {
            bookings = bookingRepository.findAllByRestaurantIdAndStartTimeIsBetween(restaurantId, spanStart, spanEnd);
        } else if (spanStart == null && spanEnd == null) {
            bookings = bookingRepository.findAllByRestaurantId(restaurantId);
        } else if (spanStart == null) {
            bookings = bookingRepository.findAllByRestaurantIdAndStartTimeIsBefore(restaurantId, spanEnd);
        } else {
            bookings = bookingRepository.findAllByRestaurantIdAndStartTimeIsAfter(restaurantId, spanStart);
        }
        return bookingMapper.mapToDto(bookings);
    }
}
