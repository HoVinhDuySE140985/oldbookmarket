package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.PostRequestDTO;
import com.example.oldbookmarket.dto.respone.PostResponeDTO;
import com.example.oldbookmarket.entity.Post;

import java.util.List;

public interface PostService {
    PostResponeDTO createPost(PostRequestDTO postRequestDTO);
    PostResponeDTO acceptPost(Long id);
    PostResponeDTO rejectPost(PostRequestDTO postRequestDTO);
    PostResponeDTO updatePostStatus(Long id);
    List<PostResponeDTO> getAllPost();
    List<PostResponeDTO> getAllMyPosts(Long userId);
    List<PostResponeDTO> searchPostByTitle(String title);
    List<PostResponeDTO> searchPostByKeyWord(String keyWord);
}
