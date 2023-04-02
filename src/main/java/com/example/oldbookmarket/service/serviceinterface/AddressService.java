package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.addressDTO.AddressRequestDTO;
import com.example.oldbookmarket.dto.request.addressDTO.UpdateAddressRequestDTO;
import com.example.oldbookmarket.dto.response.addressDTO.AddressResponseDTO;
import com.example.oldbookmarket.dto.response.addressDTO.CityResponseDTO;
import com.example.oldbookmarket.entity.Address;

import java.util.List;

public interface AddressService {
    AddressResponseDTO createNewAddress(AddressRequestDTO addressRequestDTO);

    AddressResponseDTO updateAddress(UpdateAddressRequestDTO updateAddressRequestDTO);

    Address updateAddressStatus(Long addressId);

    List<AddressResponseDTO> getAllAddress(Long userId);
    List<String> getAllCityInPosts();
}
