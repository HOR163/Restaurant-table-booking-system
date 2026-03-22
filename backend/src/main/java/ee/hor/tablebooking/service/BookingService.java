package ee.hor.tablebooking.service;

import ee.hor.tablebooking.dto.BookingDto;
import ee.hor.tablebooking.dto.BookingSlotDto;
import ee.hor.tablebooking.entity.BookingEntity;
import ee.hor.tablebooking.entity.TableEntity;
import ee.hor.tablebooking.excpetion.InvalidTimeException;
import ee.hor.tablebooking.excpetion.ResourceNotFoundException;
import ee.hor.tablebooking.excpetion.TimespanAlreadyInUseException;
import ee.hor.tablebooking.mapper.BookingMapper;
import ee.hor.tablebooking.repository.BookingRepository;
import ee.hor.tablebooking.repository.RestaurantRepository;
import ee.hor.tablebooking.repository.TableRepository;
import ee.hor.tablebooking.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    public static final int BOOKING_DURATION_IN_MINUTES = 180;
    public static final LocalTime BOOKING_MINIMUM_START_TIME = LocalTime.of(10, 0, 0);
    public static final LocalTime BOOKING_MAXIMUM_START_TIME = LocalTime.of(21, 0, 0);
    private static final int BOOKING_PENDING_MINUTES = 15;

    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final TableRepository tableRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public BookingDto getBooking(UUID id) {
        BookingEntity bookingEntity = bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Booking with given id does not exist")
        );

        return bookingMapper.mapToDto(bookingEntity);
    }

    public BookingDto addBooking(BookingDto bookingDto) {
        if (!userRepository.existsById(bookingDto.getUserId())) {
            throw new ResourceNotFoundException("User with given id does not exist");
        }
        if (!tableRepository.existsById(bookingDto.getTableId())) {
            throw new ResourceNotFoundException("Table with given id does not exist");
        }
        if (bookingDto.getStartTime().isBefore(OffsetDateTime.now())) {
            throw new InvalidTimeException("Booking date has to be after the current date");
        }

        if (!bookingRepository.findAllByTableIdAndStartTimeIsBetween(bookingDto.getTableId(),
                bookingDto.getStartTime(),
                bookingDto.getStartTime().plusMinutes(BOOKING_DURATION_IN_MINUTES)).isEmpty()) {
            throw new TimespanAlreadyInUseException("Selected timespan is already in use");
        }

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
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking with given id does not exist");
        }

        bookingRepository.deleteById(id);
    }

    public List<BookingDto> getUserBookings(UUID userId, OffsetDateTime startTime, OffsetDateTime endTime) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User with given id does not exist");
        }

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

    public Map<UUID, List<BookingSlotDto>> getRestaurantBookingSlots(UUID restaurantId, LocalDate date) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant with given id does not exist");
        }

        List<TableEntity> tables = tableRepository.findAllByRestaurantId(restaurantId);
        List<BookingEntity> bookings = bookingRepository.findAllByRestaurantIdAndStartTimeIsBetween(restaurantId,
                date.atStartOfDay().atOffset(ZoneOffset.UTC),
                date.atStartOfDay().plusDays(1).minusSeconds(1).atOffset(ZoneOffset.UTC)
        );

        bookings.sort(Comparator.comparing(BookingEntity::getStartTime));

        Map<UUID, List<BookingSlotDto>> tableFreeSlots = new HashMap<>();
        Map<UUID, LocalTime> tableCurrentSlotStart = new HashMap<>();

        for (BookingEntity booking : bookings) {
            UUID tableId = booking.getTable().getId();
            LocalTime bookingStartTime = booking.getStartTime().toLocalTime();
            LocalTime slotStartTime = tableCurrentSlotStart.getOrDefault(tableId, BOOKING_MINIMUM_START_TIME);

            if (Duration.between(slotStartTime, bookingStartTime).toMinutes() >= BOOKING_DURATION_IN_MINUTES) {
                tableFreeSlots.computeIfAbsent(tableId, k -> new ArrayList<>())
                        .add(new BookingSlotDto(slotStartTime, bookingStartTime.minusMinutes(BOOKING_DURATION_IN_MINUTES)));
            }

            // Check that with the next booking we don't go over to another day
            LocalTime newBookingStartTime = bookingStartTime.plusMinutes(BOOKING_DURATION_IN_MINUTES);
            if (newBookingStartTime.isAfter(bookingStartTime)) {
                tableCurrentSlotStart.put(tableId, newBookingStartTime);
            } else {
                tableCurrentSlotStart.put(tableId, LocalTime.of(23, 59));
            }
        }

        // Add tables that didn't have any bookings and add slots until the end of the last booking time
        for (TableEntity table : tables) {
            UUID tableId = table.getId();
            LocalTime slotStartTime = tableCurrentSlotStart.getOrDefault(tableId, BOOKING_MINIMUM_START_TIME);

            if (!slotStartTime.isAfter(BOOKING_MAXIMUM_START_TIME)) {
                tableFreeSlots.computeIfAbsent(tableId, k -> new ArrayList<>())
                        .add(new BookingSlotDto(slotStartTime, BOOKING_MAXIMUM_START_TIME));
            }
        }

        return tableFreeSlots;
    }
}
