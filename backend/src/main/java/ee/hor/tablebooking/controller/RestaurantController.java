package ee.hor.tablebooking.controller;

import ee.hor.tablebooking.dto.BookingDto;
import ee.hor.tablebooking.dto.BookingSlotDto;
import ee.hor.tablebooking.dto.RestaurantDto;
import ee.hor.tablebooking.dto.TableDto;
import ee.hor.tablebooking.service.BookingService;
import ee.hor.tablebooking.service.RestaurantService;
import ee.hor.tablebooking.service.TableService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final BookingService bookingService;
    private final RestaurantService restaurantService;
    private final TableService tableService;

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable UUID id) {
        return ResponseEntity.ok(restaurantService.getRestaurant(id));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<BookingDto>> getBookings(@PathVariable UUID id,
                                                        @RequestParam(value = "spanStart", required = false) OffsetDateTime spanStart,
                                                        @RequestParam(value = "spanEnd", required = false) OffsetDateTime spanEnd) {
        return ResponseEntity.ok(bookingService.getRestaurantBookings(id, spanStart, spanEnd));
    }

    @GetMapping("/{id}/tables")
    public ResponseEntity<List<TableDto>> getTables(@PathVariable UUID id) {
        return ResponseEntity.ok().body(tableService.getTablesInRestaurant(id));
    }

    @GetMapping("/{id}/slots")
    public ResponseEntity<Map<UUID, List<BookingSlotDto>>> getBookingSlots(@PathVariable UUID id, @RequestParam(value = "date", required = true) LocalDate date) {
        return ResponseEntity.ok().body(bookingService.getRestaurantBookingSlots(id, date));
    }

    @PostMapping
    public ResponseEntity<RestaurantDto> addRestaurant(@RequestBody RestaurantDto restaurantDto) {
        return ResponseEntity.ok(restaurantService.addRestaurant(restaurantDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable UUID id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

}
