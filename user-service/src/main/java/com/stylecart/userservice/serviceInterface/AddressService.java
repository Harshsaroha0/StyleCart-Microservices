package com.stylecart.userservice.serviceInterface;

import com.stylecart.userservice.dto.AddressRequest;
import com.stylecart.userservice.dto.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressResponse createAddress(Long userId, AddressRequest request);

    List<AddressResponse> getAllAddresses(Long userId);

    AddressResponse getAddress(Long userId, Long addressId);

    AddressResponse updateAddress(
            Long userId,
            Long addressId,
            AddressRequest request
    );

    void deleteAddress(Long userId, Long addressId);

    AddressResponse setDefaultAddress(Long userId, Long addressId);
}