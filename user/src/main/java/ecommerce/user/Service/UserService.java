package ecommerce.user.Service;

import ecommerce.user.Dto.UserDTO;
import ecommerce.user.Entity.User;
import ecommerce.user.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findAllUserDTOs() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            log.info("No users found");
            return Collections.emptyList();
        }

        log.info("Retrieved users list:");
        for (User user : users) {
            log.info("User: {}", user.getUsername());
        }

        // Convert User entities to UserDTOs
        return users.stream()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build())
                .collect(Collectors.toList());
    }

}
