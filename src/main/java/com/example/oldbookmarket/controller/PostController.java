package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.PostRequestDTO;
import com.example.oldbookmarket.dto.respone.PostResponeDTO;
import com.example.oldbookmarket.dto.respone.ResponeDTO;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    PostService postService;

    @PostMapping ("create-post")
    public ResponseEntity<ResponeDTO> createNewPost(@RequestBody PostRequestDTO postRequestDTO){
        ResponeDTO responeDTO = new ResponeDTO();
        try {
            PostResponeDTO postResponeDTO = postService.createNewPost(postRequestDTO);
            responeDTO.setData(postResponeDTO);
            responeDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(responeDTO);
    }
}
