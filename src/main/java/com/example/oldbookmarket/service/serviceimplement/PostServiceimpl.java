package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.BookExchangeRequestDTO;
import com.example.oldbookmarket.dto.request.BookSellRequestDTO;
import com.example.oldbookmarket.dto.request.PostRequestDTO;
import com.example.oldbookmarket.dto.respone.PostResponeDTO;
import com.example.oldbookmarket.entity.*;
import com.example.oldbookmarket.repository.*;
import com.example.oldbookmarket.service.serviceinterface.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceimpl implements PostService {
    @Autowired
    AddressRepo addressRepo;

    @Autowired
    PostRepo postRepo;

    @Autowired
    BookRepo bookRepo;

    @Autowired
    SubcategoryRepo subcategoryRepo;

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    BookImageRepo bookImageRepo;

    @Autowired
    UserRepo userRepo;

    @Override
    public PostResponeDTO createPost(PostRequestDTO postRequestDTO) {
        PostResponeDTO postResponeDTO = null;
        Address userAddress = addressRepo.findAddressByUser_Id(postRequestDTO.getUserId());
        User user = userRepo.getById(postRequestDTO.getUserId());
        try {
            Subcategory subcategory = subcategoryRepo.getById(postRequestDTO.getSubCategoryId());
            Post post = new Post();
            post.setSubcategory(subcategory);
            post.setImageUrl(postRequestDTO.getImageUrl());
            post.setTitle(postRequestDTO.getTitle());
            post.setForm(postRequestDTO.getForm());
            post.setCreateAt(LocalDate.now());
            post.setLocation(userAddress.getCity());
            post.setPostStatus("pending");
            post.setUser(user);
            post = postRepo.save(post);

            if(post.getForm().equalsIgnoreCase("bán")){
                List<BookSellRequestDTO> bookSellRequestDTOS = postRequestDTO.getBookSellList();
                for (BookSellRequestDTO bookSellRequestDTO : bookSellRequestDTOS) {
                    Book book = new Book();
                    book.setName(bookSellRequestDTO.getName());
                    book.setIsbn(bookSellRequestDTO.getIsbn());
                    book.setAuthor(bookSellRequestDTO.getAuthor());
                    book.setPublicationDate(bookSellRequestDTO.getPublicationDate());
                    book.setPublicCompany(bookSellRequestDTO.getPublicCompany());
                    book.setLanguage(bookSellRequestDTO.getLanguage());
                    book.setCoverType(bookSellRequestDTO.getCoverType());
                    book.setStatusQuo(bookSellRequestDTO.getStatusQuo());
                    book.setDescription(bookSellRequestDTO.getDescription());
                    book.setInitPrice(bookSellRequestDTO.getInitPrice());
                    book.setPrice(bookSellRequestDTO.getPrice());
                    book.setPost(post);
                    book = bookRepo.save(book);
                    List<String> bookImages = bookSellRequestDTO.getBookImages();
                    for (String bookImage:bookImages) {
                        BookImage _bookImage = new BookImage();
                        _bookImage.setUrl(bookImage);
                        _bookImage.setBook(book);
                        bookImageRepo.save(_bookImage);
                    }
                }
            }else {
                List<BookExchangeRequestDTO> bookExchangeRequestDTOS = postRequestDTO.getBookExchangeList();
                for (BookExchangeRequestDTO bookExchangeRequestDTO : bookExchangeRequestDTOS) {
                    Book book = new Book();
                    book.setName(bookExchangeRequestDTO.getName());
                    book.setIsbn(bookExchangeRequestDTO.getIsbn());
                    book.setAuthor(bookExchangeRequestDTO.getAuthor());
                    book.setPublicationDate(bookExchangeRequestDTO.getPublicationDate());
                    book.setPublicCompany(bookExchangeRequestDTO.getPublicCompany());
                    book.setLanguage(bookExchangeRequestDTO.getLanguage());
                    book.setCoverType(bookExchangeRequestDTO.getCoverType());
                    book.setStatusQuo(bookExchangeRequestDTO.getStatusQuo());
                    book.setDescription(bookExchangeRequestDTO.getDescription());
                    book.setPrice(bookExchangeRequestDTO.getPrice());
                    book.setCategoryExchange(bookExchangeRequestDTO.getCategoryExchange());
                    book.setPost(post);
                    book = bookRepo.save(book);
                    List<String> bookImages = bookExchangeRequestDTO.getBookImages();
                    for (String bookImage:bookImages) {
                        BookImage _bookImage = new BookImage();
                        _bookImage.setUrl(bookImage);
                        _bookImage.setBook(book);
                        bookImageRepo.save(_bookImage);
                    }
                }
            }
            Category category = postRepo.findBySubcategory_Id(post.getSubcategory().getId());
            postResponeDTO =PostResponeDTO.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .imageUrl(post.getImageUrl())
                    .form(post.getForm())
                    .categoryId(category.getId())
                    .subCategoryId(post.getSubcategory().getId())
                    .bookList(post.getBooks())
                    .location(post.getLocation())
                    .userId(post.getUser().getId())
                    .status(post.getPostStatus())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return postResponeDTO;
    }

    @Override
    public List<PostResponeDTO> getAllPost() {
        List<Post> postList = null;
        List<PostResponeDTO> postResponeDTOS = new ArrayList<>();
        try {
            postList = postRepo.findAll();
            for (Post post: postList) {
                Category category = postRepo.findBySubcategory_Id(post.getSubcategory().getId());
                if(post.getPostStatus().equalsIgnoreCase("active")){
                    PostResponeDTO postResponeDTO = new PostResponeDTO();
                    postResponeDTO.setId(post.getId());
                    postResponeDTO.setTitle(post.getTitle());
                    postResponeDTO.setForm(post.getForm());
                    postResponeDTO.setCategoryId(category.getId());
                    postResponeDTO.setSubCategoryId(post.getSubcategory().getId());
                    postResponeDTO.setImageUrl(post.getImageUrl());
                    postResponeDTO.setLocation(post.getLocation());
                    postResponeDTO.setStatus(post.getPostStatus());
                    postResponeDTO.setUserId(post.getUser().getId());
                    postResponeDTOS.add(postResponeDTO);
                }
            }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.valueOf(404),"Chua co bai dang");
        }
        return postResponeDTOS;
    }

    @Override
    public List<PostResponeDTO> getAllMyPosts(Long userId) {
        List<Post> postList = new ArrayList<>();
        List<PostResponeDTO>  myListPosts = new ArrayList<>();
        try {
            postList = postRepo.getAllByUser_Id(userId);
            for (Post posts:postList) {
                Category category = postRepo.findBySubcategory_Id(posts.getSubcategory().getId());
                PostResponeDTO postResponeDTO = new PostResponeDTO();
                postResponeDTO.setId(posts.getId());
                postResponeDTO.setTitle(posts.getTitle());
                postResponeDTO.setForm(posts.getForm());
                postResponeDTO.setCategoryId(category.getId());
                postResponeDTO.setSubCategoryId(posts.getSubcategory().getId());
                postResponeDTO.setImageUrl(posts.getImageUrl());
                postResponeDTO.setLocation(posts.getLocation());
                postResponeDTO.setStatus(posts.getPostStatus());
                postResponeDTO.setReasonReject(posts.getReasonReject());
//                postResponeDTO.setUserId(posts.getUser().getId());
                myListPosts.add(postResponeDTO);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return myListPosts;
    }

    @Override
    public List<PostResponeDTO> searchPostByTitle(String title) {
        List<Post> postList = null;
        List<PostResponeDTO> postResponeDTOS = new ArrayList<>();
        try {
            postList = postRepo.findAll();
            for (Post post: postList) {
                Category category = postRepo.findBySubcategory_Id(post.getSubcategory().getId());
                if(post.getPostStatus().equalsIgnoreCase("active")){
                    PostResponeDTO postResponeDTO = new PostResponeDTO();
                    postResponeDTO.setId(post.getId());
                    postResponeDTO.setTitle(post.getTitle());
                    postResponeDTO.setForm(post.getForm());
                    postResponeDTO.setCategoryId(category.getId());
                    postResponeDTO.setSubCategoryId(post.getSubcategory().getId());
                    postResponeDTO.setImageUrl(post.getImageUrl());
                    postResponeDTO.setLocation(post.getLocation());
                    postResponeDTO.setStatus(post.getPostStatus());
                    postResponeDTO.setUserId(post.getUser().getId());
                    postResponeDTOS.add(postResponeDTO);
                }
            }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.valueOf(404),"Khong tim thay bai dang");
        }
        return postResponeDTOS;
    }

    @Override
    public List<PostResponeDTO> searchPostByKeyWord(String keyWord) {
        List<Post> postList = null;
        List<PostResponeDTO> postResponeDTOS = new ArrayList<>();
        try {
            postList = postRepo.findAll();
            for (Post post: postList) {
                Category category = postRepo.findBySubcategory_Id(post.getSubcategory().getId());
                if(post.getPostStatus().equalsIgnoreCase("active")){
                    PostResponeDTO postResponeDTO = new PostResponeDTO();
                    postResponeDTO.setId(post.getId());
                    postResponeDTO.setTitle(post.getTitle());
                    postResponeDTO.setForm(post.getForm());
                    postResponeDTO.setCategoryId(category.getId());
                    postResponeDTO.setSubCategoryId(post.getSubcategory().getId());
                    postResponeDTO.setImageUrl(post.getImageUrl());
                    postResponeDTO.setLocation(post.getLocation());
                    postResponeDTO.setStatus(post.getPostStatus());
                    postResponeDTO.setUserId(post.getUser().getId());
                    postResponeDTOS.add(postResponeDTO);
                }
            }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.valueOf(404),"Khong tim thay bai dang");
        }
        return postResponeDTOS;
    }

    @Override
    public PostResponeDTO acceptPost(Long id) {
        PostResponeDTO postResponeDTO = new PostResponeDTO();
        try {
            Post post = postRepo.getById(id);
            post.setPostStatus("active");
            postRepo.save(post);
            Category category = postRepo.findBySubcategory_Id(post.getSubcategory().getId());
            postResponeDTO.setId(post.getId());
            postResponeDTO.setTitle(post.getTitle());
            postResponeDTO.setForm(post.getForm());
            postResponeDTO.setCategoryId(category.getId());
            postResponeDTO.setSubCategoryId(post.getSubcategory().getId());
            postResponeDTO.setImageUrl(post.getImageUrl());
            postResponeDTO.setLocation(post.getLocation());
            postResponeDTO.setStatus(post.getPostStatus());
            postResponeDTO.setUserId(post.getUser().getId());
        }catch (Exception e){
            e.printStackTrace();
        }
        return postResponeDTO;
    }

    @Override
    public PostResponeDTO rejectPost(PostRequestDTO postRequestDTO) {
        PostResponeDTO postResponeDTO = new PostResponeDTO();
        try {
            Post post = postRepo.getById(postRequestDTO.getId());
            post.setPostStatus("reject");
            post.setReasonReject(post.getReasonReject());
            postRepo.save(post);
            Category category = postRepo.findBySubcategory_Id(post.getSubcategory().getId());
            postResponeDTO.setId(post.getId());
            postResponeDTO.setTitle(post.getTitle());
            postResponeDTO.setForm(post.getForm());
            postResponeDTO.setCategoryId(category.getId());
            postResponeDTO.setSubCategoryId(post.getSubcategory().getId());
            postResponeDTO.setImageUrl(post.getImageUrl());
            postResponeDTO.setLocation(post.getLocation());
            postResponeDTO.setStatus(post.getPostStatus());
            postResponeDTO.setReasonReject(post.getReasonReject());
            postResponeDTO.setUserId(post.getUser().getId());
        }catch (Exception e){
            e.printStackTrace();
        }
        return postResponeDTO;
    }

    @Override
    public PostResponeDTO updatePostStatus(Long id) {
        PostResponeDTO postResponeDTO = new PostResponeDTO();
        try {
            Post post = postRepo.getById(id);
            if(post.getPostStatus().equalsIgnoreCase("active")){
                post.setPostStatus("deactivate");
                postRepo.save(post);
            } else if (post.getPostStatus().equalsIgnoreCase("deactivate")) {
                post.setPostStatus("active");
                postRepo.save(post);
            }
            Category category = postRepo.findBySubcategory_Id(post.getSubcategory().getId());
            postResponeDTO.setId(post.getId());
            postResponeDTO.setTitle(post.getTitle());
            postResponeDTO.setForm(post.getForm());
            postResponeDTO.setCategoryId(category.getId());
            postResponeDTO.setSubCategoryId(post.getSubcategory().getId());
            postResponeDTO.setImageUrl(post.getImageUrl());
            postResponeDTO.setLocation(post.getLocation());
            postResponeDTO.setStatus(post.getPostStatus());
//            postResponeDTO.setReasonReject(post.getReasonReject());
            postResponeDTO.setUserId(post.getUser().getId());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
