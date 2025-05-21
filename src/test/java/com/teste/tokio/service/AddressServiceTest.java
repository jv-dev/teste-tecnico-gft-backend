package com.teste.tokio.service;

import com.teste.tokio.dto.AddressRequest;
import com.teste.tokio.dto.AddressResponse;
import com.teste.tokio.model.Address;
import com.teste.tokio.model.Role;
import com.teste.tokio.model.User;
import com.teste.tokio.repository.AddressRepository;
import com.teste.tokio.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = User.builder()
                .id(1L)
                .name("Well")
                .email("well@email.com")
                .password("123")
                .role(Role.USER)
                .build();
    }

    @Test
    void shouldCreateAddressForUserSuccessfully() {
        AddressRequest request = new AddressRequest();
        request.setStreet("Rua A");
        request.setCity("São Paulo");
        request.setZipCode("01000-000");

        Address savedAddress = Address.builder()
                .id(10L)
                .street("Rua A")
                .city("São Paulo")
                .zipCode("01000-000")
                .user(mockUser)
                .build();

        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(Optional.of(mockUser));
        when(addressRepository.save(any(Address.class))).thenReturn(savedAddress);

        AddressResponse response = addressService.create(mockUser.getEmail(), request);

        assertNotNull(response);
        assertEquals("Rua A", response.getStreet());
        assertEquals("São Paulo", response.getCity());
        assertEquals("01000-000", response.getZipCode());

        verify(userRepository, times(1)).findByEmail(mockUser.getEmail());
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void shouldReturnPagedAddressesByUserId() {
        Address address1 = Address.builder()
                .id(1L)
                .street("Rua A")
                .city("São Paulo")
                .zipCode("01000-000")
                .user(mockUser)
                .build();

        Address address2 = Address.builder()
                .id(2L)
                .street("Rua B")
                .city("Rio de Janeiro")
                .zipCode("20000-000")
                .user(mockUser)
                .build();

        Pageable pageable = PageRequest.of(0, 2);
        Page<Address> page = new PageImpl<>(List.of(address1, address2), pageable, 2);

        when(addressRepository.findByUserId(mockUser.getId(), pageable)).thenReturn(page);

        var result = addressService.findAllByUserId(mockUser.getId(), pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("Rua A", result.getContent().get(0).getStreet());
        assertEquals("Rua B", result.getContent().get(1).getStreet());
        assertEquals(0, result.getPage());
        assertEquals(2, result.getSize());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());

        verify(addressRepository, times(1)).findByUserId(mockUser.getId(), pageable);
    }

}
