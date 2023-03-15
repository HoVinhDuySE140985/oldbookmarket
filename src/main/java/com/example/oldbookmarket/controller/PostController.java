package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.PostRequestDTO;
import com.example.oldbookmarket.dto.respone.PostResponeDTO;
import com.example.oldbookmarket.dto.respone.ResponeDTO;
import com.example.oldbookmarket.entity.Post;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    PostService postService;

    @PostMapping ("create-post")
    public ResponseEntity<ResponeDTO> creatlPost(@RequestBody PostRequestDTO postRequestDTO){
        ResponeDTO responeDTO = new ResponeDTO();
        try {
            PostResponeDTO postResponeDTO = postService.createPost(postRequestDTO);
            responeDTO.setData(postResponeDTO);
            responeDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(responeDTO);
    }

    @GetMapping("get-all-post")
    public ResponseEntity<ResponeDTO> getAllPost(){
        ResponeDTO responeDTO = new ResponeDTO();
        try {
            List<PostResponeDTO> postList = postService.getAllPost();
            responeDTO.setData(postList);
            responeDTO.setSuccessCode(SuccessCode.Get_All_Success);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(responeDTO);
    }

    @GetMapping("get-all-my-post/{userId}")
    public ResponseEntity<ResponeDTO> getAllMyPost(long userId){
        ResponeDTO responeDTO = new ResponeDTO();
        try {
            List<PostResponeDTO> myPostList = postService.getAllMyPosts(userId);
            responeDTO.setData(myPostList);
            responeDTO.setSuccessCode(SuccessCode.Get_All_Success);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(responeDTO);
    }

    @GetMapping("search-post-by-title/{title}")
    public ResponseEntity<ResponeDTO> searchPostByBookName(@PathVariable String title){
        ResponeDTO responeDTO = new ResponeDTO();
        try {
            List<PostResponeDTO> resultList = postService.searchPostByTitle(title);
            responeDTO.setData(resultList);
            responeDTO.setSuccessCode(SuccessCode.FOUND_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(responeDTO);
    }

    @GetMapping("search-post-by-Keyword/{keyWord}")
    public ResponseEntity<ResponeDTO> searchPostByKeyWord(@PathVariable String keyWord){
        ResponeDTO responeDTO = new ResponeDTO();
        try {
            List<PostResponeDTO> resultList = postService.searchPostByKeyWord(keyWord);
            responeDTO.setData(resultList);
            responeDTO.setSuccessCode(SuccessCode.FOUND_SUCCESS);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(responeDTO);
    }

    @PutMapping("accept-post/{id}")
    public ResponseEntity<ResponeDTO> acceptPost(@PathVariable Long id){
        ResponeDTO responeDTO = new ResponeDTO();
        try {
            PostResponeDTO post = postService.acceptPost(id);
            responeDTO.setData(post);
            responeDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(responeDTO);
    }

    @PutMapping("reject-post/{id}")
    public ResponseEntity<ResponeDTO> rejectPost(@RequestBody PostRequestDTO postRequestDTO){
        ResponeDTO responeDTO = new ResponeDTO();
        try {
            PostResponeDTO post = postService.rejectPost(postRequestDTO);
            responeDTO.setData(post);
            responeDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(responeDTO);
    }

    @PutMapping("update-post-status/{id}")
    public ResponseEntity<ResponeDTO> updatePostStatus(Long id){
        ResponeDTO responeDTO = new ResponeDTO();
        try {
            PostResponeDTO postResponeDTO = postService.updatePostStatus(id);
            responeDTO.setData(postResponeDTO);
            responeDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return  ResponseEntity.ok().body(responeDTO);
    }
}
