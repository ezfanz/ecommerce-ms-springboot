package ecommerce.user.Service;

import ecommerce.user.Dto.UserDTO;
import ecommerce.user.Entity.User;
import ecommerce.user.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Transactional(readOnly = true)
    public List<UserDTO> findAllUserDTOs() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            log.info("No users found");
            return Collections.emptyList();
        }

        log.info("Retrieved users list:");
        users.forEach(user -> log.info("User: {}", user.getUsername()));

        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> findUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public UserDTO saveUser(UserDTO userDTO) {
        User user = User.builder()
                .username(userDTO.getUserName())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .email(userDTO.getEmail())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .build();
        userRepository.save(user);
        return convertToDTO(user);
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .build();
    }
}
