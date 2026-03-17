package ee.hor.tablebooking.service;

import ee.hor.tablebooking.dto.UserDto;
import ee.hor.tablebooking.entity.UserEntity;
import ee.hor.tablebooking.mapper.UserMapper;
import ee.hor.tablebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto getUser(UUID id) {
        // FIXME: throw an error if not found
        UserEntity user = userRepository.findById(id).orElse(null);

        return userMapper.mapToDto(user);
    }

    // TODO: create another service for signup and login

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
