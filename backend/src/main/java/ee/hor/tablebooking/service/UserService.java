package ee.hor.tablebooking.service;

import ee.hor.tablebooking.dto.UserDto;
import ee.hor.tablebooking.entity.UserEntity;
import ee.hor.tablebooking.excpetion.EntityInUseException;
import ee.hor.tablebooking.excpetion.ResourceNotFoundException;
import ee.hor.tablebooking.mapper.UserMapper;
import ee.hor.tablebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto getUser(UUID id) {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("user with given id does not exist")
        );

        return userMapper.mapToDto(user);
    }

    // TODO: create another service for signup and login

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking with given id does not exist");
        }

        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException _) {
            throw new EntityInUseException("User is being used by other objects, remove them before proceeding with deletion");
        }
    }
}
