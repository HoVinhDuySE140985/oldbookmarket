package com.example.oldbookmarket.controller;

import com.example.oldbookmarket.dto.request.postDTO.PostRequestDTO;
import com.example.oldbookmarket.dto.response.addressDTO.CityResponseDTO;
import com.example.oldbookmarket.dto.response.postDTO.PostResponseDTO;
import com.example.oldbookmarket.dto.response.ResponseDTO;
import com.example.oldbookmarket.enumcode.SuccessCode;
import com.example.oldbookmarket.service.serviceinterface.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    PostService postService;

    @PostMapping("create-post")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> creatlPost(@RequestBody @Validated PostRequestDTO postRequestDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            PostResponseDTO postResponseDTO = postService.createPost(postRequestDTO);
            responseDTO.setData(postResponseDTO);
            responseDTO.setSuccessCode(SuccessCode.CREATE_SUCCESS);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(responseDTO);
    }

//    @GetMapping("get-all-post") // nhung post có trang thái active
//    @PermitAll
//    public ResponseEntity<ResponseDTO> getAllPost( @RequestParam(required = false) String sortBy,
//                                                   @RequestParam(required = false) String filter) {
//        ResponseDTO responseDTO = new ResponseDTO();
//        try {
//            List<PostResponseDTO> postList = postService.getAllPost(sortBy,filter);
//            responseDTO.setData(postList);
//            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
//            responseDTO.setResult(postList.size());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.ok().body(responseDTO);
//    }

    @GetMapping("get_all_my_post_by_userId")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> getAllMylPost(@RequestParam @Validated Long userId) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<PostResponseDTO> mySellPostList = postService.getAllPosts(userId);
            responseDTO.setData(mySellPostList);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
            responseDTO.setResult(mySellPostList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get_all_post_no_condition") // lấy post ko can có diều kiện trạng thái
    @PermitAll
    public ResponseEntity<ResponseDTO> getAllPostNoCondition(){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<PostResponseDTO> postList = postService.getAllPostNoCondition();
            responseDTO.setData(postList);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("search-post-by-Keyword")
    @PermitAll
    public ResponseEntity<ResponseDTO> searchPostByKeyWord(@RequestParam(required = false) @Validated String keyWord,
                                                           @RequestParam(required = false) @Validated String sortBy,
                                                           @RequestParam(required = false) @Validated  String filter) {
        ResponseDTO responseDTO = new ResponseDTO();
        List<PostResponseDTO> resultList = postService.searchPostByKeyWord(keyWord,sortBy,filter);
        try {
            responseDTO.setData(resultList);
            responseDTO.setResult(resultList.size());
            responseDTO.setSuccessCode(SuccessCode.FOUND_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("staff/accept-post/{id}")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public ResponseEntity<ResponseDTO> acceptPost(@PathVariable @Validated Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            PostResponseDTO post = postService.acceptPost(id);
            responseDTO.setData(post);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("staff/reject-post/{id}")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public ResponseEntity<ResponseDTO> rejectPost(@PathVariable @Validated Long id, @RequestParam @Validated String reasonReject) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            PostResponseDTO post = postService.rejectPost(id, reasonReject);
            responseDTO.setData(post);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-post-status/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> updatePostStatus(@PathVariable @Validated Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            PostResponseDTO postResponseDTO = postService.updatePostStatus(id);
            responseDTO.setData(postResponseDTO);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("update-post-info")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> updatePostInfo(@RequestBody @Validated PostRequestDTO postRequestDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            PostResponseDTO postResponseDTO = postService.updatePostInfo(postRequestDTO);
            responseDTO.setData(postResponseDTO);
            responseDTO.setSuccessCode(SuccessCode.UPDATE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get_all_post_by_subcategoryId")
    @PermitAll
    public ResponseEntity<ResponseDTO> getAllPostBySubCategory(@RequestParam @Validated Long subcategoryId,
                                                               @RequestParam(required = false) @Validated String sortBy,
                                                               @RequestParam(required = false) @Validated String filter){
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            List<PostResponseDTO> postResponseDTOs = postService.getAllPostBySubcategory(subcategoryId,sortBy,filter);
            responseDTO.setData(postResponseDTOs);
            responseDTO.setSuccessCode(SuccessCode.Get_All_Success);
            responseDTO.setResult(postResponseDTOs.size());
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseDTO);
    }

}
