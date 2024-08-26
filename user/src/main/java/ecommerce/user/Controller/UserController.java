package ecommerce.user.Controller;

import ecommerce.user.Dto.UserDTO;
import ecommerce.user.Service.UserService;

import ecommerce.user.Util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor


public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        try {
            List<UserDTO> userDTOs = userService.findAllUserDTOs();
            return ResponseHandler.generateResponse(HttpStatus.OK, false, "Users retrieved successfully", userDTOs);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

}






