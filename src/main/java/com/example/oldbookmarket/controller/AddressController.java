package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.addressDTO.AddressRequestDTO;
import com.example.oldbookmarket.dto.request.addressDTO.UpdateAddressRequestDTO;
import com.example.oldbookmarket.dto.response.addressDTO.AddressResponseDTO;
import com.example.oldbookmarket.dto.response.ResponseDTO;
import com.example.oldbookmarket.dto.response.addressDTO.CityResponseDTO;
import com.example.oldbookmarket.entity.Address;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    AddressService addressService;

    @PostMapping("create-new-address")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> createNewAddress(@RequestBody @Validated AddressRequestDTO addressRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            AddressResponseDTO addressResponseDTO = addressService.createNewAddress(addressRequestDTO);
            responseDTO.setData(addressResponseDTO);
            responseDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-address")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> updateAddress(@RequestBody @Validated UpdateAddressRequestDTO updateAddressRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            AddressResponseDTO addressResponseDTO = addressService.updateAddress(updateAddressRequestDTO);
            responseDTO.setData(addressResponseDTO);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-address-status/{addressId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> updateAddressStatus(@PathVariable @Validated Long addressId){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Address address = addressService.updateAddressStatus(addressId);
            responseDTO.setData(address);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-address-By/{userId}")
    @PermitAll
    public ResponseEntity<ResponseDTO> getAllAdress(@PathVariable @Validated Long userId){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<AddressResponseDTO> addressList = addressService.getAllAddress(userId);
            responseDTO.setData(addressList);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get_all_city_in_list_post")
    @PermitAll
    public ResponseEntity<ResponseDTO> getAllCityInPosts(){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<String> listCities = addressService.getAllCityInPosts();
            responseDTO.setData(listCities);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  ResponseEntity.ok().body(responseDTO);
    }
}
