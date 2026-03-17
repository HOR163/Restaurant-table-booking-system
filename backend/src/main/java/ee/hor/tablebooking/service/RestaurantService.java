package ee.hor.tablebooking.service;

import ee.hor.tablebooking.dto.RestaurantDto;
import ee.hor.tablebooking.entity.RestaurantEntity;
import ee.hor.tablebooking.mapper.RestaurantMapper;
import ee.hor.tablebooking.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;
    private final RestaurantRepository restaurantRepository;

    public RestaurantDto getRestaurant(UUID id) {
        // FIXME: throw an error
        RestaurantEntity restaurant = restaurantRepository.findById(id).orElse(null);

        return restaurantMapper.mapToDto(restaurant);
    }

    public List<RestaurantDto> getAllRestaurants() {
        List<RestaurantEntity> restaurants = restaurantRepository.findAll();

        return restaurantMapper.mapToDto(restaurants);
    }

    public RestaurantDto addRestaurant(RestaurantDto restaurantDto) {
        RestaurantEntity restaurantEntity = restaurantMapper.mapToEntity(restaurantDto);

        RestaurantEntity newRestaurant = restaurantRepository.save(restaurantEntity);

        return restaurantMapper.mapToDto(newRestaurant);
    }

    public void deleteRestaurant(UUID id) {
        restaurantRepository.deleteById(id);
    }
}
