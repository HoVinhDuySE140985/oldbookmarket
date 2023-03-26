package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.bookDTO.BookRequestDTO;
import com.example.oldbookmarket.dto.request.postDTO.PostRequestDTO;
import com.example.oldbookmarket.dto.respone.PostResponseDTO;
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
    public PostResponseDTO createPost(PostRequestDTO postRequestDTO) {
        PostResponseDTO postResponseDTO = null;
        User user = userRepo.getById(postRequestDTO.getUserId());
        try {
            Subcategory subcategory = subcategoryRepo.getById(postRequestDTO.getSubCategoryId());
            Post post = new Post();
            post.setSubcategory(subcategory);
            post.setImageUrl(postRequestDTO.getImageUrl());
            post.setTitle(postRequestDTO.getTitle());
            post.setForm(postRequestDTO.getForm());
            post.setCreateAt(LocalDate.now());
            post.setInitPrice(postRequestDTO.getInitPrice());
            post.setPrice(postRequestDTO.getPrice());
            post.setDescription(postRequestDTO.getDescription());
            post.setBookExchange(postRequestDTO.getBookExchange());
            post.setLocation(postRequestDTO.getLocation());
            post.setPostStatus("pending");
            post.setUser(user);
            post = postRepo.save(post);

            List<BookRequestDTO> bookRequestDTOS = postRequestDTO.getBookList();
            for (BookRequestDTO bookRequestDTO : bookRequestDTOS) {
                Book book = new Book();
                book.setName(bookRequestDTO.getName());
                book.setIsbn(bookRequestDTO.getIsbn());
                book.setAuthor(bookRequestDTO.getAuthor());
                book.setPublicationDate(bookRequestDTO.getPublicationDate());
                book.setPublicCompany(bookRequestDTO.getPublicCompany());
                book.setLanguage(bookRequestDTO.getLanguage());
                book.setCoverType(bookRequestDTO.getCoverType());
                book.setStatusQuo(bookRequestDTO.getStatusQuo());
//                    book.setDescription(bookRequestDTO.getDescription());
                book.setPost(post);
                book = bookRepo.save(book);
                List<String> bookImages = bookRequestDTO.getBookImages();
                for (String bookImage : bookImages) {
                    BookImage _bookImage = new BookImage();
                    _bookImage.setUrl(bookImage);
                    _bookImage.setBook(book);
                    bookImageRepo.save(_bookImage);
                }
            }

//            Subcategory subcate = subcategoryRepo.getById(post.getSubcategory().getId());
            postResponseDTO = PostResponseDTO.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .imageUrl(post.getImageUrl())
                    .form(post.getForm())
//                    .categoryId(subcate.getCategory().getId())
//                    .subCategoryId(post.getSubcategory().getId())
//                    .bookList(post.getBooks())
                    .price(post.getPrice())
                    .description(post.getDescription())
                    .location(post.getLocation())
                    .userId(post.getUser().getId())
                    .status(post.getPostStatus())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postResponseDTO;
    }

    @Override
    public List<PostResponseDTO> getAllPost() {
        List<Post> postList = null;
        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();
        try {
            postList = postRepo.findAll();
            for (Post post : postList) {
                if (post.getPostStatus().equalsIgnoreCase("active")) {
                    PostResponseDTO postResponseDTO = new PostResponseDTO();
                    postResponseDTO.setId(post.getId());
                    postResponseDTO.setTitle(post.getTitle());
                    postResponseDTO.setForm(post.getForm());
//                    postResponeDTO.setCategoryId(subcate.getCategory().getId());
//                    postResponeDTO.setSubCategoryId(post.getSubcategory().getId());
                    postResponseDTO.setBookList(post.getBooks());
                    postResponseDTO.setImageUrl(post.getImageUrl());
                    postResponseDTO.setLocation(post.getLocation());
                    postResponseDTO.setPrice(post.getPrice());
                    postResponseDTO.setUserName(post.getUser().getName());
                    postResponseDTO.setStatus(post.getPostStatus());
                    postResponseDTO.setUserId(post.getUser().getId());
                    postResponseDTOS.add(postResponseDTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.valueOf(404), "Chua co bai dang");
        }
        return postResponseDTOS;
    }

    @Override
    public List<PostResponseDTO> getAllMyPosts(Long userId) {
        List<Post> postList = null;
        List<PostResponseDTO> myListPosts = new ArrayList<>();
        try {
            postList = postRepo.getAllByUser_Id(userId);
            for (Post post : postList) {
                PostResponseDTO postResponseDTO = new PostResponseDTO();
                postResponseDTO.setId(post.getId());
                postResponseDTO.setTitle(post.getTitle());
                postResponseDTO.setForm(post.getForm());
//                postResponeDTO.setCategoryId(subcate.getCategory().getId());
//                postResponeDTO.setSubCategoryId(posts.getSubcategory().getId());
                postResponseDTO.setBookList(post.getBooks());
                postResponseDTO.setImageUrl(post.getImageUrl());
                postResponseDTO.setLocation(post.getLocation());
                postResponseDTO.setPrice(post.getPrice());
                postResponseDTO.setStatus(post.getPostStatus());
//                postResponeDTO.setReasonReject(posts.getReasonReject());
                postResponseDTO.setUserId(post.getUser().getId());
                myListPosts.add(postResponseDTO);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myListPosts;
    }

//    @Override
//    public PostResponseDTO searchPostByTitle(String title) {
//        List<Post> postList = null;
//        PostResponseDTO postResponseDTO = new PostResponseDTO();
//        try {
//            postList = postRepo.findAllByTitle(title);
//            for (Post post: postList) {
//                if(post.getPostStatus().equalsIgnoreCase("active")){
//                    postResponseDTO.setId(post.getId());
//                    postResponseDTO.setTitle(post.getTitle());
//                    postResponseDTO.setForm(post.getForm());
//                    postResponseDTO.setImageUrl(post.getImageUrl());
//                    postResponseDTO.setLocation(post.getLocation());
//                    postResponseDTO.setPrice(post.getPrice());
//                    postResponseDTO.setStatus(post.getPostStatus());
//                    postResponseDTO.setUserId(post.getUser().getId());
//                }
//            }
//        }catch (Exception e){
//            throw new ResponseStatusException(HttpStatus.valueOf(404),"Khong tim thay bai dang");
//        }
//        return postResponseDTO;
//    }

    @Override
    public List<PostResponseDTO> searchPostByKeyWord(String keyWord) {
        List<Post> postList = null;
        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();
        try {
            postList = postRepo.findByKeyWord(keyWord);
            for (Post post : postList) {
                if (post.getPostStatus().equalsIgnoreCase("active")) {
                    PostResponseDTO postResponseDTO = new PostResponseDTO();
                    postResponseDTO.setId(post.getId());
                    postResponseDTO.setTitle(post.getTitle());
                    postResponseDTO.setForm(post.getForm());
                    postResponseDTO.setImageUrl(post.getImageUrl());
                    postResponseDTO.setLocation(post.getLocation());
                    postResponseDTO.setBookList(post.getBooks());
                    postResponseDTO.setPrice(post.getPrice());
                    postResponseDTO.setStatus(post.getPostStatus());
                    postResponseDTO.setUserId(post.getUser().getId());
                    postResponseDTO.setUserName(post.getUser().getName());
                    postResponseDTOS.add(postResponseDTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postResponseDTOS;
    }

    @Override
    public PostResponseDTO acceptPost(Long id) {
        PostResponseDTO postResponseDTO = new PostResponseDTO();
        try {
            Post post = postRepo.getById(id);
            post.setPostStatus("active");
            postRepo.save(post);
            Subcategory subcate = subcategoryRepo.getById(post.getSubcategory().getId());
            postResponseDTO.setId(post.getId());
            postResponseDTO.setTitle(post.getTitle());
            postResponseDTO.setForm(post.getForm());
//            postResponeDTO.setCategoryId(subcate.getCategory().getId());
//            postResponeDTO.setSubCategoryId(post.getSubcategory().getId());
//            postResponeDTO.setBookList(post.getBooks());
            postResponseDTO.setImageUrl(post.getImageUrl());
            postResponseDTO.setLocation(post.getLocation());
            postResponseDTO.setPrice(post.getPrice());
            postResponseDTO.setStatus(post.getPostStatus());
            postResponseDTO.setUserId(post.getUser().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postResponseDTO;
    }

    @Override
    public PostResponseDTO rejectPost(Long id, String reasonReject) {
        PostResponseDTO postResponseDTO = new PostResponseDTO();
        try {
            Post post = postRepo.getById(id);
            post.setPostStatus("reject");
            post.setReasonReject(reasonReject);
            postRepo.save(post);
            Subcategory subcate = subcategoryRepo.getById(post.getSubcategory().getId());
            postResponseDTO.setId(post.getId());
            postResponseDTO.setTitle(post.getTitle());
            postResponseDTO.setForm(post.getForm());
//            postResponeDTO.setCategoryId(subcate.getCategory().getId());
//            postResponeDTO.setSubCategoryId(post.getSubcategory().getId());
//            postResponeDTO.setBookList(post.getBooks());
            postResponseDTO.setImageUrl(post.getImageUrl());
            postResponseDTO.setPrice(post.getPrice());
            postResponseDTO.setLocation(post.getLocation());
            postResponseDTO.setStatus(post.getPostStatus());
//            postResponeDTO.setReasonReject(post.getReasonReject());
            postResponseDTO.setUserId(post.getUser().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postResponseDTO;
    }

    @Override
    public PostResponseDTO updatePostStatus(Long id) {
        PostResponseDTO postResponseDTO = new PostResponseDTO();
        try {
            Post post = postRepo.getById(id);
            if (post.getPostStatus().equalsIgnoreCase("active")) {
                post.setPostStatus("deactivate");
                postRepo.save(post);
            } else if (post.getPostStatus().equalsIgnoreCase("deactivate")) {
                post.setPostStatus("active");
                postRepo.save(post);
            }
//            Subcategory subcate = subcategoryRepo.getById(post.getSubcategory().getId());
            postResponseDTO.setId(post.getId());
            postResponseDTO.setTitle(post.getTitle());
            postResponseDTO.setForm(post.getForm());
//            postResponeDTO.setCategoryId(subcate.getCategory().getId());
//            postResponeDTO.setSubCategoryId(post.getSubcategory().getId());
//            postResponeDTO.setBookList(post.getBooks());
            postResponseDTO.setImageUrl(post.getImageUrl());
            postResponseDTO.setLocation(post.getLocation());
            postResponseDTO.setPrice(post.getPrice());
            postResponseDTO.setStatus(post.getPostStatus());
//            postResponeDTO.setReasonReject(post.getReasonReject());
            postResponseDTO.setUserId(post.getUser().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postResponseDTO;
    }

    @Override
    public PostResponseDTO updatePostInfo(PostRequestDTO postRequestDTO) {
        PostResponseDTO postResponseDTO = new PostResponseDTO();
        try {
            Post post = postRepo.getById(postRequestDTO.getId());
            post.setTitle(postRequestDTO.getTitle());
            post.setForm(postRequestDTO.getForm());
            post.setImageUrl(postRequestDTO.getImageUrl());
            post.setLocation(postRequestDTO.getLocation());
            post.setPrice(postRequestDTO.getPrice());
            post.setDescription(postRequestDTO.getDescription());
            postRepo.save(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postResponseDTO;
    }

    @Override
    public List<PostResponseDTO> getAllPostBySubcategory(Long subcategoryId) {
        List<Post> postList = null;
        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();
        try {
            postList = postRepo.findAllBySubcategory_Id(subcategoryId);
            for (Post post : postList) {
                if (post.getPostStatus().equalsIgnoreCase("active")) {
                    PostResponseDTO postResponseDTO = new PostResponseDTO();
                    postResponseDTO.setId(post.getId());
                    postResponseDTO.setTitle(post.getTitle());
                    postResponseDTO.setForm(post.getForm());
                    postResponseDTO.setBookList(post.getBooks());
                    postResponseDTO.setImageUrl(post.getImageUrl());
                    postResponseDTO.setLocation(post.getLocation());
                    postResponseDTO.setPrice(post.getPrice());
                    postResponseDTO.setUserName(post.getUser().getName());
                    postResponseDTO.setStatus(post.getPostStatus());
                    postResponseDTO.setUserId(post.getUser().getId());
                    postResponseDTOS.add(postResponseDTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postResponseDTOS;
    }
}
