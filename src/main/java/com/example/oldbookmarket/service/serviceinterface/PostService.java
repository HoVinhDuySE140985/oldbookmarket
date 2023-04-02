package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.postDTO.PostRequestDTO;
import com.example.oldbookmarket.dto.response.postDTO.PostResponseDTO;

import java.util.List;

public interface PostService {
    PostResponseDTO createPost(PostRequestDTO postRequestDTO);
    PostResponseDTO acceptPost(Long id);
    PostResponseDTO rejectPost(Long id, String reasonReject);
    PostResponseDTO updatePostStatus(Long id);
    PostResponseDTO updatePostInfo(PostRequestDTO postRequestDTO);
    List<PostResponseDTO> getAllPost(String sortBy, String filter);
    List<PostResponseDTO> getAllPostNoCondition();
    List<PostResponseDTO> getAllMyPosts(Long userId);
    List<PostResponseDTO> getAllPostBySubcategory(Long subcategoryId);
    List<PostResponseDTO> searchPostByKeyWord(String keyWord,String sortBy, String filter);
}
