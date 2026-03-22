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

    /**
     * Add a booking
     *
     * @throws ResourceNotFoundException if user or table doesn't exist
     * @throws TimespanAlreadyInUseException if the timespan is already booked
     * @throws InvalidTimeException if the selected time is before the current time
     *
     * @param bookingDto DTO for the booking
     * @return the saved booking DTO, if the booking succeeded
     */
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

    /**
     * Delete a booking
     *
     * @throws ResourceNotFoundException if booking with given id does not exist
     *
     * @param id id of the booking to be deleted
     */
    public void deleteBooking(UUID id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking with given id does not exist");
        }

        bookingRepository.deleteById(id);
    }

    /**
     * Get bookings in timespan for given user
     *
     * @throws ResourceNotFoundException if user with given id was not found
     *
     * @param userId the user whose bookings will be found
     * @param startTime time to start the search (inclusive)
     * @param endTime time to end the search (inclusive)
     * @return All the bookings in given timespan
     */
    public List<BookingDto> getUserBookings(UUID userId, OffsetDateTime startTime, OffsetDateTime endTime) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User with given id does not exist");
        }

        List<BookingEntity> bookings = bookingRepository.findAllByUserIdAndStartTimeBetween(userId, startTime, endTime);
        return bookingMapper.mapToDto(bookings);
    }

    /**
     * Get all bookings in given restaurant inside an optional timespan
     *
     * If start is null, then all bookings until end are returned
     * If end is null, then all bookings starting from start are returned
     * If both start and end are null, then all the bookings are returned
     *
     * @param restaurantId id of the restaurant to be
     * @param spanStart start time of the span (null if not specified)
     * @param spanEnd end time of the span (null if not specified)
     * @return list of all bookings inside given timespan
     */
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

    /**
     * Get a map of all available booking slots in given restaurant
     *
     * The start and end times for spans from where the booking can start. As a single booking lasts
     * BOOKING_DURATION_IN_MINUTES minutes, then that time will be subtracted from the time between two bookings.
     * For example if we have two bookings starting at 10.00 and other one at 17.00, then the returned timespan is 13.00
     * to 14.00. This is because the first booking takes 3 hours, and the newly created booking would also take 3 hours,
     * thus the last available starting time is 14.00.
     *
     * The first booking of the day can start at BOOKING_MINIMUM_START_TIME and the last booking of the day can start at
     * BOOKING_MAXIMUM_START_TIME. Bookings that cross days are not supported
     *
     * @throws ResourceNotFoundException if the specified restaurant does not exist
     *
     * @param restaurantId id of the restaurant to search for
     * @param date date for which the slots should be found
     * @return map of tableIds for keys and list of available booking slots for values
     */
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
