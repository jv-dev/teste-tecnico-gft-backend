package com.teste.tokio.controller;

import com.teste.tokio.dto.AddressPageResponse;
import com.teste.tokio.dto.AddressRequest;
import com.teste.tokio.dto.AddressResponse;
import com.teste.tokio.model.Role;
import com.teste.tokio.repository.UserRepository;
import com.teste.tokio.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final UserRepository userRepository;

    @GetMapping("/user/{userId}")
    public AddressPageResponse getAddressesByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return addressService.findAllByUserId(userId, page, size, sortBy, direction, email);
    }


    @GetMapping("/{id}")
    public AddressResponse getById(@PathVariable Long id, Authentication authentication) {
        return addressService.findById(id, authentication.getName());
    }

    @PutMapping("/{id}")
    public AddressResponse update(
            @PathVariable Long id,
            @RequestBody AddressRequest request,
            Authentication authentication
    ) {
        return addressService.update(id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        addressService.delete(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public AddressResponse create(@RequestBody AddressRequest request, Authentication authentication) {
        return addressService.create(authentication.getName(), request);
    }
}
