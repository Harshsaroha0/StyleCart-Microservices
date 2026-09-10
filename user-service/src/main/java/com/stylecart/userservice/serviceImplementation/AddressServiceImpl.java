package com.stylecart.userservice.serviceImplementation;

import com.stylecart.userservice.dto.AddressRequest;
import com.stylecart.userservice.dto.AddressResponse;
import com.stylecart.userservice.entity.Address;
import com.stylecart.userservice.entity.User;
import com.stylecart.userservice.exception.NameAlreadyExistsException;
import com.stylecart.userservice.exception.ResourceNotFoundException;
import com.stylecart.userservice.repository.AddressRepository;
import com.stylecart.userservice.repository.UserRepository;
import com.stylecart.userservice.serviceInterface.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public AddressResponse createAddress(Long userId, AddressRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User does not exist with id: " + userId
                        )
                );
        Address address = new Address();
        address.setUser(user);
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setAddressType(request.getAddressType());
        address.setDefault(request.isDefault());

        Address savedAddress = addressRepository.save(address);

        AddressResponse response = new AddressResponse();

        response.setId(savedAddress.getId());
        response.setAddressLine1(savedAddress.getAddressLine1());
        response.setAddressLine2(savedAddress.getAddressLine2());
        response.setCity(savedAddress.getCity());
        response.setState(savedAddress.getState());
        response.setPostalCode(savedAddress.getPostalCode());
        response.setAddressType(savedAddress.getAddressType());
        response.setDefault(savedAddress.isDefault());

        return response;
    }

    @Override
    public List<AddressResponse> getAllAddresses(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not exists" + userId
                        )
                );

        List<Address> addresses = addressRepository.findByUserId(userId);

        return addresses.stream()
                .map(address -> {
                    AddressResponse response = new AddressResponse();

                    response.setId(address.getId());
                    response.setAddressLine1(address.getAddressLine1());
                    response.setAddressLine2(address.getAddressLine2());
                    response.setCity(address.getCity());
                    response.setState(address.getState());
                    response.setPostalCode(address.getPostalCode());
                    response.setAddressType(address.getAddressType());
                    response.setDefault(address.isDefault()
                    );

                    return response;
                })
                .toList();
    }

    @Override
    public AddressResponse getAddress(Long userId, Long addressId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not exists" + userId
                        )
                );

        Address address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address does not exist with id: " + addressId
                        )
                );

        AddressResponse response = new AddressResponse();
        response.setId(address.getId());
        response.setAddressLine1(address.getAddressLine1());
        response.setAddressLine2(address.getAddressLine2());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setPostalCode(address.getPostalCode());
        response.setAddressType(address.getAddressType());
        response.setDefault(address.isDefault());

        return response;
    }

    @Override
    public AddressResponse updateAddress(Long userId, Long addressId, AddressRequest request) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User does not exist with id: " + userId
                        )
                );

        Address address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address does not exist with id: " + addressId
                        )
                );

        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setAddressType(request.getAddressType());
        address.setDefault(request.isDefault());

        Address updatedAddress = addressRepository.save(address);

        AddressResponse response = new AddressResponse();
        response.setId(updatedAddress.getId());
        response.setAddressLine1(updatedAddress.getAddressLine1());
        response.setAddressLine2(updatedAddress.getAddressLine2());
        response.setCity(updatedAddress.getCity());
        response.setState(updatedAddress.getState());
        response.setPostalCode(updatedAddress.getPostalCode());
        response.setAddressType(updatedAddress.getAddressType());
        response.setDefault(updatedAddress.isDefault());

        return response;
    }

    @Override
    public void deleteAddress(Long userId, Long addressId) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User does not exist with id: " + userId
                        )
                );

        Address address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address does not exist with id: " + addressId
                        )
                );

        addressRepository.delete(address);
    }

    @Override
    public AddressResponse setDefaultAddress(Long userId, Long addressId) {

        userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(
                        "User does not exist with id: " + userId
                )
        );

        Address address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address does not exist with id: " + addressId
                        )
                );

        List<Address> addresses = addressRepository.findByUserId(userId);

        for (Address existingAddress : addresses) {
            existingAddress.setDefault(false);
        }

        address.setDefault(true);
        addressRepository.saveAll(addresses);
        AddressResponse response = new AddressResponse();
        response.setId(address.getId());
        response.setAddressLine1(address.getAddressLine1());
        response.setAddressLine2(address.getAddressLine2());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setPostalCode(address.getPostalCode());
        response.setAddressType(address.getAddressType());
        response.setDefault(address.isDefault());

        return response;
    }

}


