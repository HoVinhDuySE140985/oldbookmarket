package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.bookDTO.BookRequestDTO;
import com.example.oldbookmarket.dto.request.postDTO.PostRequestDTO;
import com.example.oldbookmarket.dto.response.bookDTO.BookPendingResponseDTO;
import com.example.oldbookmarket.dto.response.postDTO.PostResponseDTO;
import com.example.oldbookmarket.entity.*;
import com.example.oldbookmarket.repository.*;
import com.example.oldbookmarket.service.serviceinterface.BookAuthorRepo;
import com.example.oldbookmarket.service.serviceinterface.PostService;
import org.hibernate.annotations.SortType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

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

    @Autowired
    BookAuthorRepo bookAuthorRepo;

    @Override
    public PostResponseDTO createPost(PostRequestDTO postRequestDTO) {
        PostResponseDTO postResponseDTO = null;
        User user = userRepo.getById(postRequestDTO.getUserId());
        try {
            Subcategory subcategory = subcategoryRepo.findById(postRequestDTO.getSubCategoryId()).get();
            Post post = new Post();
            post.setSubcategory(subcategory);
            post.setImageUrl(postRequestDTO.getImageUrl());
            post.setTitle(postRequestDTO.getTitle());
            post.setForm(postRequestDTO.getForm());
            post.setCreateAt(LocalDate.now());
            post.setInitPrice(postRequestDTO.getInitPrice());
            post.setPrice(postRequestDTO.getPrice());
            post.setBookExchange(postRequestDTO.getBookExchange());
            post.setLocation(postRequestDTO.getLocation());
            post.setPostStatus("pending");
            post.setUser(user);
            post = postRepo.save(post);

            List<BookRequestDTO> bookRequestDTOS = postRequestDTO.getBookList();
            for (BookRequestDTO bookRequestDTO : bookRequestDTOS) {
                BookAuthor bookAuthor = bookAuthorRepo.findByName(bookRequestDTO.getAuthor());
                if (bookAuthor!=null){
                    Book book = new Book();
                    book.setName(bookRequestDTO.getName());
                    book.setIsbn(bookRequestDTO.getIsbn());
                    book.setReprints(bookRequestDTO.getReprints());
                    book.setBookAuthor(bookAuthor);
                    book.setPublicationDate(bookRequestDTO.getPublicationDate());
                    book.setPublicCompany(bookRequestDTO.getPublicCompany());
                    book.setLanguage(bookRequestDTO.getLanguage());
                    book.setCoverType(bookRequestDTO.getCoverType());
                    book.setStatusQuo(bookRequestDTO.getStatusQuo());
                    book.setDescription(bookRequestDTO.getDescription());
                    book.setPost(post);
                    book = bookRepo.save(book);
                    List<String> bookImages = bookRequestDTO.getBookImages();
                    for (String bookImage : bookImages) {
                        BookImage _bookImage = new BookImage();
                        _bookImage.setUrl(bookImage);
                        _bookImage.setBook(book);
                        bookImageRepo.save(_bookImage);
                    }
                }else {
                    BookAuthor author = BookAuthor.builder()
                            .name(bookRequestDTO.getAuthor())
                            .build();
                    author = bookAuthorRepo.save(author);
                    Book book = new Book();
                    book.setName(bookRequestDTO.getName());
                    book.setIsbn(bookRequestDTO.getIsbn());
                    book.setBookAuthor(author);
                    book.setReprints(bookRequestDTO.getReprints());
                    book.setPublicationDate(bookRequestDTO.getPublicationDate());
                    book.setPublicCompany(bookRequestDTO.getPublicCompany());
                    book.setLanguage(bookRequestDTO.getLanguage());
                    book.setCoverType(bookRequestDTO.getCoverType());
                    book.setStatusQuo(bookRequestDTO.getStatusQuo());
                    book.setDescription(bookRequestDTO.getDescription());
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
                postResponseDTO = PostResponseDTO.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .imageUrl(post.getImageUrl())
                        .form(post.getForm())
                        .price(post.getPrice())
                        .location(post.getLocation())
                        .userId(post.getUser().getId())
                        .status(post.getPostStatus())
                        .build();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postResponseDTO;
    }

    @Override
    public List<PostResponseDTO> getAllPosts(String email) {
        List<Post> postList = null;
        List<PostResponseDTO> mySellListPosts = new ArrayList<>();
        try {
            postList = postRepo.findAll();
            for (Post post : postList) {
                if (post.getUser().getEmail().equals(email)){
                    PostResponseDTO postResponseDTO = new PostResponseDTO();
                    postResponseDTO.setId(post.getId());
                    postResponseDTO.setTitle(post.getTitle());
                    postResponseDTO.setForm(post.getForm());
                    postResponseDTO.setImageUrl(post.getImageUrl());
                    postResponseDTO.setLocation(post.getLocation());
                    postResponseDTO.setInitPrice(post.getInitPrice());
                    postResponseDTO.setPrice(post.getPrice());
                    postResponseDTO.setStatus(post.getPostStatus());
                    postResponseDTO.setUserId(post.getUser().getId());
                    postResponseDTO.setUserName(post.getUser().getName());

                    List<Book> bookList = post.getBooks();
                    List<BookPendingResponseDTO> bookPendingResponseDTOS = new ArrayList<>();
                    for (Book book : bookList) {
                        BookPendingResponseDTO responseDTO = BookPendingResponseDTO.builder()
                                .bookId(book.getId())
                                .name(book.getName())
                                .coverType(book.getCoverType())
                                .description(book.getDescription())
                                .isbn(book.getIsbn())
                                .publicationDate(book.getPublicationDate())
                                .bookExchange(post.getBookExchange())
                                .publicCompany(book.getPublicCompany())
                                .statusQuo(book.getStatusQuo())
                                .language(book.getLanguage())
//                                .author(book.getAuthor())
                                .imageBook(book.getImageList())
                                .build();
                        bookPendingResponseDTOS.add(responseDTO);
                    }
                    postResponseDTO.setBookList(bookPendingResponseDTOS);
                    mySellListPosts.add(postResponseDTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mySellListPosts;
    }


    @Override
    public List<PostResponseDTO> getAllPostNoCondition() {
        List<Post> postList = null;
        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();
        try {
            postList = postRepo.findAll();
            for (Post post : postList) {
                PostResponseDTO postResponseDTO = new PostResponseDTO();
                postResponseDTO.setId(post.getId());
                postResponseDTO.setTitle(post.getTitle());
                postResponseDTO.setForm(post.getForm());
                postResponseDTO.setImageUrl(post.getImageUrl());
                postResponseDTO.setLocation(post.getLocation());
                postResponseDTO.setPrice(post.getPrice());
                postResponseDTO.setStatus(post.getPostStatus());
                postResponseDTO.setUserId(post.getUser().getId());
                postResponseDTO.setUserName(post.getUser().getName());

                List<Book> bookList = post.getBooks();
                List<BookPendingResponseDTO> bookPendingResponseDTOS = new ArrayList<>();
                for (Book book : bookList) {
                    BookPendingResponseDTO responseDTO = BookPendingResponseDTO.builder()
                            .bookId(book.getId())
                            .name(book.getName())
                            .coverType(book.getCoverType())
                            .description(book.getDescription())
                            .isbn(book.getIsbn())
                            .reprints(book.getReprints())
                            .publicationDate(book.getPublicationDate())
                            .bookExchange(post.getBookExchange())
                            .publicCompany(book.getPublicCompany())
                            .statusQuo(book.getStatusQuo())
                            .language(book.getLanguage())
                            .author(book.getBookAuthor().getName())
                            .imageBook(book.getImageList())
                            .build();
                    bookPendingResponseDTOS.add(responseDTO);
                }
                postResponseDTO.setBookList(bookPendingResponseDTOS);
                postResponseDTOS.add(postResponseDTO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return postResponseDTOS;
    }

    @Override
    public List<PostResponseDTO> searchPostByKeyWord(String keyWord, String sortBy, String filter) {
        List<Post> postList = new ArrayList<>();
        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();
        try {
            //nếu key null và sort null
            if (keyWord.equalsIgnoreCase("null") && sortBy.equalsIgnoreCase("null")) {
                postList = postRepo.findAll();
            }
            //nếu key null và sort tăng
            if (keyWord.equalsIgnoreCase("null") && sortBy.equalsIgnoreCase("tăng dần")) {
                postList = postRepo.findAll(Sort.by("price").ascending());
            }
            //nếu key null và sort giảm
            if (keyWord.equalsIgnoreCase("null") && sortBy.equalsIgnoreCase("giảm dần")) {
                postList = postRepo.findAll(Sort.by("price").descending());
            }
            //nếu key != null và sort null
            if (!keyWord.equalsIgnoreCase("null") && sortBy.equalsIgnoreCase("null")) {
                postList = postRepo.findByKeyWord(keyWord);
            }
            // nếu key != null và sort tăng
            if (!keyWord.equalsIgnoreCase("null") && sortBy.equalsIgnoreCase("tăng dần")) {
                postList = postRepo.findByKeyWord(keyWord, Sort.by("price").ascending());
            }
            // nếu key!= null và sort giam
            if (!keyWord.equalsIgnoreCase("null") && sortBy.equalsIgnoreCase("giảm dần")) {
                postList = postRepo.findByKeyWord(keyWord, Sort.by("price").descending());
            }
            if (!filter.equalsIgnoreCase("null")) {
                for (Post post : postList) {
                    if (post.getPostStatus().equalsIgnoreCase("active") && post.getLocation().equalsIgnoreCase(filter)) {
                        PostResponseDTO postResponseDTO = new PostResponseDTO();
                        postResponseDTO.setId(post.getId());
                        postResponseDTO.setTitle(post.getTitle());
                        postResponseDTO.setForm(post.getForm());
                        postResponseDTO.setImageUrl(post.getImageUrl());
                        postResponseDTO.setLocation(post.getLocation());
                        postResponseDTO.setPrice(post.getPrice());
                        postResponseDTO.setStatus(post.getPostStatus());
                        postResponseDTO.setUserId(post.getUser().getId());
                        postResponseDTO.setUserName(post.getUser().getName());
                        List<Book> bookList = post.getBooks();
                        List<BookPendingResponseDTO> bookPendingResponseDTOS = new ArrayList<>();
                        for (Book book : bookList) {
                            BookPendingResponseDTO responseDTO = BookPendingResponseDTO.builder()
                                    .bookId(book.getId())
                                    .name(book.getName())
                                    .coverType(book.getCoverType())
                                    .description(book.getDescription())
                                    .isbn(book.getIsbn())
                                    .publicationDate(book.getPublicationDate())
                                    .bookExchange(post.getBookExchange())
                                    .publicCompany(book.getPublicCompany())
                                    .statusQuo(book.getStatusQuo())
                                    .language(book.getLanguage())
//                                    .author(book.getAuthor())
                                    .imageBook(book.getImageList())
                                    .build();
                            bookPendingResponseDTOS.add(responseDTO);
                        }
                        postResponseDTO.setBookList(bookPendingResponseDTOS);
                        postResponseDTOS.add(postResponseDTO);
                    }
                }
            } else {
                for (Post post : postList) {
                    if (post.getPostStatus().equalsIgnoreCase("active")) {
                        PostResponseDTO postResponseDTO = new PostResponseDTO();
                        postResponseDTO.setId(post.getId());
                        postResponseDTO.setTitle(post.getTitle());
                        postResponseDTO.setForm(post.getForm());
                        postResponseDTO.setImageUrl(post.getImageUrl());
                        postResponseDTO.setLocation(post.getLocation());
                        postResponseDTO.setPrice(post.getPrice());
                        postResponseDTO.setStatus(post.getPostStatus());
                        postResponseDTO.setUserId(post.getUser().getId());
                        postResponseDTO.setUserName(post.getUser().getName());
                        List<Book> bookList = post.getBooks();
                        List<BookPendingResponseDTO> bookPendingResponseDTOS = new ArrayList<>();
                        for (Book book : bookList) {
                            BookPendingResponseDTO responseDTO = BookPendingResponseDTO.builder()
                                    .bookId(book.getId())
                                    .name(book.getName())
                                    .coverType(book.getCoverType())
                                    .description(book.getDescription())
                                    .isbn(book.getIsbn())
                                    .publicationDate(book.getPublicationDate())
                                    .bookExchange(post.getBookExchange())
                                    .publicCompany(book.getPublicCompany())
                                    .statusQuo(book.getStatusQuo())
                                    .language(book.getLanguage())
//                                    .author(book.getAuthor())
                                    .imageBook(book.getImageList())
                                    .build();
                            bookPendingResponseDTOS.add(responseDTO);
                        }
                        postResponseDTO.setBookList(bookPendingResponseDTOS);
                        postResponseDTOS.add(postResponseDTO);
                    }
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
            Post post = postRepo.findById(id).get();
            post.setPostStatus("active");
            postRepo.save(post);
            postResponseDTO.setId(post.getId());
            postResponseDTO.setTitle(post.getTitle());
            postResponseDTO.setForm(post.getForm());
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
            postRepo.save(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postResponseDTO;
    }

    @Override
    public List<PostResponseDTO> getAllPostBySubcategory(Long subcategoryId, String sortBy, String filter) {
        List<Post> postList = null;
        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();
        try {
            if (sortBy.equalsIgnoreCase("null")) {
                postList = postRepo.findAllBySubcategory_Id(subcategoryId);
            }
            if (sortBy.equalsIgnoreCase("tăng dần")) {
                postList = postRepo.findAllBySubcategory_Id(subcategoryId, Sort.by("price").ascending());
            }
            if (sortBy.equalsIgnoreCase("giảm dần")) {
                postList = postRepo.findAllBySubcategory_Id(subcategoryId, Sort.by("price").descending());
            }
            if (!filter.equalsIgnoreCase("null")) {
                for (Post post : postList) {
                    if (post.getPostStatus().equalsIgnoreCase("active") && post.getLocation().equalsIgnoreCase(filter)) {
                        PostResponseDTO postResponseDTO = new PostResponseDTO();
                        postResponseDTO.setId(post.getId());
                        postResponseDTO.setTitle(post.getTitle());
                        postResponseDTO.setForm(post.getForm());
                        postResponseDTO.setImageUrl(post.getImageUrl());
                        postResponseDTO.setLocation(post.getLocation());
                        postResponseDTO.setPrice(post.getPrice());
                        postResponseDTO.setStatus(post.getPostStatus());
                        postResponseDTO.setUserId(post.getUser().getId());
                        postResponseDTO.setUserName(post.getUser().getName());

                        List<Book> bookList = post.getBooks();
                        List<BookPendingResponseDTO> bookPendingResponseDTOS = new ArrayList<>();
                        for (Book book : bookList) {
                            BookPendingResponseDTO responseDTO = BookPendingResponseDTO.builder()
                                    .bookId(book.getId())
                                    .name(book.getName())
                                    .coverType(book.getCoverType())
                                    .description(book.getDescription())
                                    .isbn(book.getIsbn())
                                    .publicationDate(book.getPublicationDate())
                                    .bookExchange(post.getBookExchange())
                                    .publicCompany(book.getPublicCompany())
                                    .statusQuo(book.getStatusQuo())
                                    .language(book.getLanguage())
//                                    .author(book.getAuthor())
                                    .imageBook(book.getImageList())
                                    .build();
                            bookPendingResponseDTOS.add(responseDTO);
                        }
                        postResponseDTO.setBookList(bookPendingResponseDTOS);
                        postResponseDTOS.add(postResponseDTO);
                    }
                }
            } else {
                for (Post post : postList) {
                    if (post.getPostStatus().equalsIgnoreCase("active")) {
                        PostResponseDTO postResponseDTO = new PostResponseDTO();
                        postResponseDTO.setId(post.getId());
                        postResponseDTO.setTitle(post.getTitle());
                        postResponseDTO.setForm(post.getForm());
                        postResponseDTO.setImageUrl(post.getImageUrl());
                        postResponseDTO.setLocation(post.getLocation());
                        postResponseDTO.setPrice(post.getPrice());
                        postResponseDTO.setStatus(post.getPostStatus());
                        postResponseDTO.setUserId(post.getUser().getId());
                        postResponseDTO.setUserName(post.getUser().getName());
                        List<Book> bookList = post.getBooks();
                        List<BookPendingResponseDTO> bookPendingResponseDTOS = new ArrayList<>();
                        for (Book book : bookList) {
                            BookPendingResponseDTO responseDTO = BookPendingResponseDTO.builder()
                                    .bookId(book.getId())
                                    .name(book.getName())
                                    .coverType(book.getCoverType())
                                    .description(book.getDescription())
                                    .isbn(book.getIsbn())
                                    .publicationDate(book.getPublicationDate())
                                    .bookExchange(post.getBookExchange())
                                    .publicCompany(book.getPublicCompany())
                                    .statusQuo(book.getStatusQuo())
                                    .language(book.getLanguage())
//                                    .author(book.getAuthor())
                                    .imageBook(book.getImageList())
                                    .build();
                            bookPendingResponseDTOS.add(responseDTO);
                        }
                        postResponseDTO.setBookList(bookPendingResponseDTOS);
                        postResponseDTOS.add(postResponseDTO);
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return postResponseDTOS;
    }

}
