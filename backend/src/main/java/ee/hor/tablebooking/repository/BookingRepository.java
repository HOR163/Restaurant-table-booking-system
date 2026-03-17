package ee.hor.tablebooking.repository;

import ee.hor.tablebooking.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<BookingEntity, UUID> {
    List<BookingEntity> findAllByUserIdAndStartTimeBetween(UUID id, OffsetDateTime spanStart, OffsetDateTime spanEnd);

    @Query("SELECT b FROM BookingEntity b " +
            "JOIN TableEntity t " +
            "JOIN RestaurantEntity r " +
            "WHERE r.id = :restaurantId " +
            "AND b.startTime BETWEEN :spanStart AND :spanEnd")
    List<BookingEntity> findAllByRestaurantIdAndStartTimeIsBetween(UUID restaurantId, OffsetDateTime spanStart, OffsetDateTime spanEnd);

    @Query("SELECT b FROM BookingEntity b JOIN TableEntity t JOIN RestaurantEntity r WHERE r.id = :restaurantId")
    List<BookingEntity> findAllByRestaurantId(UUID restaurantId);

    @Query("SELECT b FROM BookingEntity b JOIN TableEntity t JOIN RestaurantEntity r WHERE r.id = :restaurantId AND b.startTime > :spanStart")
    List<BookingEntity> findAllByRestaurantIdAndStartTimeIsAfter(UUID restaurantId, OffsetDateTime spanStart);

    @Query("SELECT b FROM BookingEntity b JOIN TableEntity t JOIN RestaurantEntity r WHERE r.id = :restaurantId AND b.startTime < :spanEnd")
    List<BookingEntity> findAllByRestaurantIdAndStartTimeIsBefore(UUID restaurantId, OffsetDateTime spanEnd);
}
