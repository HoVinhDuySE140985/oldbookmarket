package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.addressDTO.AddressRequestDTO;
import com.example.oldbookmarket.dto.request.addressDTO.UpdateAddressRequestDTO;
import com.example.oldbookmarket.dto.response.addressDTO.AddressResponseDTO;
import com.example.oldbookmarket.dto.response.addressDTO.CityResponseDTO;
import com.example.oldbookmarket.entity.Address;
import com.example.oldbookmarket.entity.Post;
import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.repository.AddressRepo;
import com.example.oldbookmarket.repository.PostRepo;
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
    PostRepo postRepo;

    @Autowired
    UserRepo userRepo;

    @Override
    public AddressResponseDTO createNewAddress(AddressRequestDTO addressRequestDTO) {
        AddressResponseDTO addressResponseDTO = null;
        User user = null;
        try {
            user = userRepo.findById(addressRequestDTO.getUserId()).get();
            if (user.getUserStatus().equals("active")) {
                Boolean f = false;
                user = userRepo.getById(addressRequestDTO.getUserId());
                List<Address> result = addressRepo.findAllByUser_Id(addressRequestDTO.getUserId());
                for (Address address : result) {
                    if (address.getProvince().equalsIgnoreCase(addressRequestDTO.getProvince()) && address.getCity().equalsIgnoreCase(addressRequestDTO.getCity()) &&
                            address.getWard().equalsIgnoreCase(addressRequestDTO.getWard()) && address.getDistrict().equalsIgnoreCase(addressRequestDTO.getDistrict()) &&
                            address.getStreet().equalsIgnoreCase(addressRequestDTO.getStreet()) && address.getStatus().equalsIgnoreCase("deactive")) {
                        address.setStatus("active");
                        addressRepo.save(address);
                        addressResponseDTO = AddressResponseDTO.builder()
                                .id(address.getId())
                                .city(address.getCity())
                                .province(address.getProvince())
                                .district(address.getDistrict())
                                .ward(address.getWard())
                                .street(address.getStreet())
                                .build();
                        f = true;
                    }
                }
                if (!f) {
                    Address address = new Address();
                    address.setCity(addressRequestDTO.getCity());
                    address.setDistrict(addressRequestDTO.getDistrict());
                    address.setProvince(addressRequestDTO.getProvince());
                    address.setWard(addressRequestDTO.getWard());
                    address.setStreet(addressRequestDTO.getStreet());
                    address.setStatus("active");
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addressResponseDTO;
    }

    @Override
    public AddressResponseDTO updateAddress(UpdateAddressRequestDTO updateAddressRequestDTO) {
        AddressResponseDTO addressResponseDTO = null;
        User user = null;
        try {
            Address address = addressRepo.getById(updateAddressRequestDTO.getAddressId());
            if (address != null) {
                address.setCity(updateAddressRequestDTO.getCity());
                address.setProvince(updateAddressRequestDTO.getProvince());
                address.setDistrict(updateAddressRequestDTO.getDistrict());
                address.setWard(updateAddressRequestDTO.getWard());
                address.setStreet(updateAddressRequestDTO.getStreet());
                address = addressRepo.save(address);
                addressResponseDTO = AddressResponseDTO.builder()
                        .id(address.getId())
                        .city(address.getCity())
                        .province(address.getProvince())
                        .district(address.getDistrict())
                        .ward(address.getWard())
                        .street(address.getStreet())
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addressResponseDTO;
    }

    @Override
    public Address updateAddressStatus(Long addressId) {
        Address address = new Address();
        try {
            address = addressRepo.getById(addressId);
            if (address != null) {
                address.setStatus("DEACTIVATE");
                address = addressRepo.save(address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    public List<AddressResponseDTO> getAllAddress(Long userId) {
        List<Address> addressList = new ArrayList<>();
        List<AddressResponseDTO> addressResponseDTOS = new ArrayList<>();
        try {
            addressList = addressRepo.findAllByUser_Id(userId);
            for (Address address : addressList) {
                if (address.getStatus().equalsIgnoreCase("active")) {
                    AddressResponseDTO addressResponseDTO = AddressResponseDTO.builder()
                            .id(address.getId())
                            .province(address.getProvince())
                            .city(address.getCity())
                            .ward(address.getWard())
                            .district(address.getDistrict())
                            .street(address.getStreet())
                            .Status(address.getStatus())
                            .build();
                    addressResponseDTOS.add(addressResponseDTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addressResponseDTOS;
    }

    @Override
    public List<String> getAllCityInPosts() {
        List<String> listCities = new ArrayList<>();
        try {
            listCities = postRepo.findAllCity();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCities;
    }
}
