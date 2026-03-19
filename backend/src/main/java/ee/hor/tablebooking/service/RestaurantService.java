package ee.hor.tablebooking.service;

import ee.hor.tablebooking.dto.RestaurantDto;
import ee.hor.tablebooking.entity.RestaurantEntity;
import ee.hor.tablebooking.excpetion.EntityInUseException;
import ee.hor.tablebooking.excpetion.ResourceNotFoundException;
import ee.hor.tablebooking.mapper.RestaurantMapper;
import ee.hor.tablebooking.repository.RestaurantRepository;
import ee.hor.tablebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantDto getRestaurant(UUID id) {
        RestaurantEntity restaurant = restaurantRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Restaurant with given id does not exist")
        );

        return restaurantMapper.mapToDto(restaurant);
    }

    public List<RestaurantDto> getAllRestaurants() {
        List<RestaurantEntity> restaurants = restaurantRepository.findAll();

        return restaurantMapper.mapToDto(restaurants);
    }

    public RestaurantDto addRestaurant(RestaurantDto restaurantDto) {
        if (!userRepository.existsById(restaurantDto.getOwnerId())) {
            throw new ResourceNotFoundException("User with given id does not exist");
        }

        RestaurantEntity restaurantEntity = restaurantMapper.mapToEntity(restaurantDto);

        RestaurantEntity newRestaurant = restaurantRepository.save(restaurantEntity);

        return restaurantMapper.mapToDto(newRestaurant);
    }

    public void deleteRestaurant(UUID id) {
        if (!restaurantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Restaurant with given id does not exist");
        }
        try {
            restaurantRepository.deleteById(id);
        } catch (DataIntegrityViolationException _) {
            throw new EntityInUseException("Table is being used by other objects, remove them before proceeding with deletion");
        }
    }
}
