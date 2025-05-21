package com.teste.tokio.service;

import com.teste.tokio.dto.AddressPageResponse;
import com.teste.tokio.dto.AddressRequest;
import com.teste.tokio.dto.AddressResponse;
import com.teste.tokio.model.Address;
import com.teste.tokio.model.Role;
import com.teste.tokio.model.User;
import com.teste.tokio.repository.AddressRepository;
import com.teste.tokio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressPageResponse findAllByUserId(Long userId, int page, int size, String sortBy, String direction, String requesterEmail) {
        User requester = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isOwner = requester.getId().equals(userId);
        boolean isAdmin = requester.getRole().equals(Role.ADMIN);

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Not authorized to access these addresses.");
        }

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Address> pageResult = addressRepository.findByUserId(userId, pageRequest);

        List<AddressResponse> content = pageResult.stream()
                .map(this::toResponse)
                .toList();

        return new AddressPageResponse(
                content,
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages()
        );
    }


    public AddressResponse create(String email, AddressRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow();

        Address address = Address.builder()
                .street(request.getStreet())
                .number(request.getNumber())
                .complement(request.getComplement())
                .district(request.getDistrict())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .user(user)
                .build();

        return toResponse(addressRepository.save(address));
    }

    public AddressResponse findById(Long id, String email) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        User requester = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isOwner = address.getUser().getId().equals(requester.getId());
        boolean isAdmin = requester.getRole().equals(Role.ADMIN);

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Not authorized to access this address.");
        }

        return toResponse(address);
    }

    public AddressResponse update(Long id, AddressRequest request, String email) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        User requester = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isOwner = address.getUser().getId().equals(requester.getId());
        boolean isAdmin = requester.getRole().equals(Role.ADMIN);

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Not authorized to update this address.");
        }

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setZipCode(request.getZipCode());

        Address updated = addressRepository.save(address);
        return toResponse(updated);
    }

    public void delete(Long id, String email) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        User requester = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isOwner = address.getUser().getId().equals(requester.getId());
        boolean isAdmin = requester.getRole().equals(Role.ADMIN);

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Not authorized to delete this address.");
        }

        addressRepository.delete(address);
    }

    private AddressResponse toResponse(Address address) {
        AddressResponse dto = new AddressResponse();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setNumber(address.getNumber());
        dto.setComplement(address.getComplement());
        dto.setDistrict(address.getDistrict());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setZipCode(address.getZipCode());
        dto.setCreatedAt(address.getCreatedAt());
        return dto;
    }
}
