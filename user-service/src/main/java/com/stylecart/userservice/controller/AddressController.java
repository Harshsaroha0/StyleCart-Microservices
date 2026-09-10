package com.stylecart.userservice.controller;


import com.stylecart.userservice.dto.AddressRequest;
import com.stylecart.userservice.dto.AddressResponse;
import com.stylecart.userservice.serviceInterface.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users/{userId}/addresses")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(
            @PathVariable Long userId,
            @Valid @RequestBody AddressRequest request) {

        AddressResponse response =
                addressService.createAddress(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAllAddresses(
            @PathVariable Long userId) {

        List<AddressResponse> response =
                addressService.getAllAddresses(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponse> getAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId) {

        AddressResponse response =
                addressService.getAddress(userId, addressId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request) {

        AddressResponse response =
                addressService.updateAddress(
                        userId,
                        addressId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId) {

        addressService.deleteAddress(userId, addressId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{addressId}/default")
    public ResponseEntity<AddressResponse> setDefaultAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId) {

        AddressResponse response =
                addressService.setDefaultAddress(userId, addressId);

        return ResponseEntity.ok(response);
    }
}