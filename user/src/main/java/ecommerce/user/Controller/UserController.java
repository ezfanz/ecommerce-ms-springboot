package ecommerce.user.Controller;

import ecommerce.user.Dto.UserDTO;
import ecommerce.user.Service.UserService;
import ecommerce.user.Util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        try {
            List<UserDTO> userDTOs = userService.findAllUserDTOs();
            return ResponseHandler.generateResponse(HttpStatus.OK, false, "Users retrieved successfully", userDTOs);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Integer id) {
        try {
            return userService.findUserById(id)
                    .map(userDTO -> ResponseHandler.generateResponse(HttpStatus.OK, false, "User retrieved successfully", userDTO))
                    .orElseGet(() -> ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, "User not found", null));
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, true, "An error occurred: " + e.getMessage(), null);
        }
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO savedUser = userService.saveUser(userDTO);
            return ResponseHandler.generateResponse(HttpStatus.CREATED, false, "User created successfully", savedUser);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, "User creation failed: " + e.getMessage(), null);
        }
    }
}
