package com.usermanagement.service.user.controller;

import com.usermanagement.service.user.entity.User;
import com.usermanagement.service.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.annotations.*;

@RestController
@RequestMapping("/api/users") // Change the base request mapping to /api/users
@Api(value = "User Management", tags = "User Management") // Update the API value and tags
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @ApiOperation(value = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = User.class),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ResponseEntity<User> getUserById(@PathVariable @ApiParam(value = "ID of the user", example = "123") Long id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping
    @ApiOperation(value = "Get all users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update user by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ResponseEntity<User> updateUser(@PathVariable @ApiParam(value = "ID of the user", example = "123") Long id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete user by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    public ResponseEntity<Void> deleteUser(@PathVariable @ApiParam(value = "ID of the user", example = "123") Long id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
