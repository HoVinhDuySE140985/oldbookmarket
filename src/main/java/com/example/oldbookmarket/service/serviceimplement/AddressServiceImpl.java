package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.addressDTO.AddressRequestDTO;
import com.example.oldbookmarket.dto.request.addressDTO.UpdateAddressRequestDTO;
import com.example.oldbookmarket.dto.respone.AddressResponseDTO;
import com.example.oldbookmarket.entity.Address;
import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.repository.AddressRepo;
import com.example.oldbookmarket.repository.UserRepo;
import com.example.oldbookmarket.service.serviceinterface.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressRepo addressRepo;

    @Autowired
    UserRepo userRepo;
    @Override
    public AddressResponseDTO createNewAddress(AddressRequestDTO addressRequestDTO) {
        AddressResponseDTO addressResponseDTO = null;
        try {
            User user = userRepo.getById(addressRequestDTO.getUserId());
            Address address = new Address();
            address.setCity(addressRequestDTO.getCity());
            address.setDistrict(addressRequestDTO.getDistrict());
            address.setProvince(addressRequestDTO.getProvince());
            address.setWard(addressRequestDTO.getWard());
            address.setStreet(addressRequestDTO.getStreet());
            address.setUser(user);
            address = addressRepo.save(address);
            addressResponseDTO = AddressResponseDTO.builder()
                    .id(address.getId())
                    .city(address.getCity())
                    .province(address.getProvince())
                    .district(address.getDistrict())
                    .ward(address.getWard())
                    .street(address.getStreet())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return addressResponseDTO;
    }

    @Override
    public AddressResponseDTO updateAddress(UpdateAddressRequestDTO updateAddressRequestDTO) {
        AddressResponseDTO addressResponseDTO = null;
        try {
            Address address = addressRepo.getById(updateAddressRequestDTO.getAddressId());
            if(address != null){
                address.setCity(updateAddressRequestDTO.getCity());
                address.setProvince(updateAddressRequestDTO.getProvince());
                address.setDistrict(updateAddressRequestDTO.getDistrict());
                address.setWard(updateAddressRequestDTO.getWard());
                address.setStreet(updateAddressRequestDTO.getWard());
                address = addressRepo.save(address);
                addressResponseDTO = AddressResponseDTO.builder()
                        .city(address.getCity())
                        .province(address.getProvince())
                        .district(address.getDistrict())
                        .ward(address.getWard())
                        .street(address.getStreet())
                        .build();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return addressResponseDTO;
    }

    @Override
    public Address updateAddressStatus(Long addressId) {
        Address address = new Address();
        try {
            address = addressRepo.getById(addressId);
            if (address!=null){
                address.setStatus("DEFAULT");
                address = addressRepo.save(address);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return address;
    }

    @Override
    public List<Address> getAllAddress(Long userId) {
        List<Address> addressList = null;
        try {
            addressList = addressRepo.findAllByUser_Id(userId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return addressList;
    }
}
