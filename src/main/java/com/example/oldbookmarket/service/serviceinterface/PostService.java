package com.example.oldbookmarket.service.serviceinterface;

import com.example.oldbookmarket.dto.request.PostRequestDTO;
import com.example.oldbookmarket.dto.respone.PostResponeDTO;

public interface PostService {
    PostResponeDTO createNewPost(PostRequestDTO postRequestDTO);
}
