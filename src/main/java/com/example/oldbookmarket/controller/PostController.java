package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.PostRequestDTO;
import com.example.oldbookmarket.dto.respone.PostResponseDTO;
import com.example.oldbookmarket.dto.respone.ResponseDTO;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.PostService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<ResponseDTO> creatlPost(@RequestBody PostRequestDTO postRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            PostResponseDTO postResponseDTO = postService.createPost(postRequestDTO);
            responseDTO.setData(postResponseDTO);
            responseDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-post")
    public ResponseEntity<ResponseDTO> getAllPost(){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<PostResponseDTO> postList = postService.getAllPost();
            responseDTO.setData(postList);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-my-post/{userId}")
    public ResponseEntity<ResponseDTO> getAllMyPost(long userId){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<PostResponseDTO> myPostList = postService.getAllMyPosts(userId);
            responseDTO.setData(myPostList);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("search-post-by-title/{title}")
    public ResponseEntity<ResponseDTO> searchPostByBookName(@PathVariable String title){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<PostResponseDTO> resultList = postService.searchPostByTitle(title);
            responseDTO.setData(resultList);
            responseDTO.setSuccessCode(SuccessCode.FOUND_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("search-post-by-Keyword/{keyWord}")
    public ResponseEntity<ResponseDTO> searchPostByKeyWord(@PathVariable String keyWord){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<PostResponseDTO> resultList = postService.searchPostByKeyWord(keyWord);
            responseDTO.setData(resultList);
            responseDTO.setSuccessCode(SuccessCode.FOUND_SUCCESS);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("accept-post/{id}")
    public ResponseEntity<ResponseDTO> acceptPost(@PathVariable Long id){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            PostResponseDTO post = postService.acceptPost(id);
            responseDTO.setData(post);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("reject-post/{id}")
    public ResponseEntity<ResponseDTO> rejectPost(@PathVariable Long id, String reasonReject){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            PostResponseDTO post = postService.rejectPost(id,reasonReject);
            responseDTO.setData(post);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-post-status/{id}")
    public ResponseEntity<ResponseDTO> updatePostStatus(Long id){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            PostResponseDTO postResponseDTO = postService.updatePostStatus(id);
            responseDTO.setData(postResponseDTO);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return  ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-post-info")
    public ResponseEntity<ResponseDTO> updatePostInfo(@RequestBody PostRequestDTO postRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            PostResponseDTO postResponseDTO = postService.updatePostInfo(postRequestDTO);
            responseDTO.setData(postResponseDTO);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return  ResponseEntity.ok().body(responseDTO);
    }
}
