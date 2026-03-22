package ee.hor.tablebooking.controller;

import ee.hor.tablebooking.dto.BookingDto;
import ee.hor.tablebooking.dto.RegisterUserDto;
import ee.hor.tablebooking.dto.UserDto;
import ee.hor.tablebooking.service.BookingService;
import ee.hor.tablebooking.service.UserService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final BookingService bookingService;
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<BookingDto>> getBookings(@PathVariable UUID id,
                                                        @RequestParam("spanStart") OffsetDateTime spanStart,
                                                        @RequestParam("spanEnd") OffsetDateTime spanEnd) {
        return ResponseEntity.ok(bookingService.getUserBookings(id, spanStart, spanEnd));
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterUserDto registerUserDto) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
