package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.dto.CreateBookRequest;
import com.bookstore.onlinebookstore.dto.CreateUserRequest;
import com.bookstore.onlinebookstore.entity.Book;
import com.bookstore.onlinebookstore.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest request) {
        adminService.createUser(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@RequestBody CreateBookRequest request) {
        return ResponseEntity.ok(adminService.createBook(request));
    }
}
