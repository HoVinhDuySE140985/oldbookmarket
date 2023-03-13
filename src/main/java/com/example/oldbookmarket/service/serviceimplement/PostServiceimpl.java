package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.BookRequestDTO;
import com.example.oldbookmarket.dto.request.PostRequestDTO;
import com.example.oldbookmarket.dto.respone.PostResponeDTO;
import com.example.oldbookmarket.entity.*;
import com.example.oldbookmarket.repository.*;
import com.example.oldbookmarket.service.serviceinterface.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public PostResponeDTO createNewPost(PostRequestDTO postRequestDTO) {
        PostResponeDTO postResponeDTO = null;
        List<Book> bookList = new ArrayList<>();

        Address userAddress = addressRepo.findAddressByUser_Id(postRequestDTO.getUserId());
        User user = userRepo.getById(postRequestDTO.getUserId());
        try {
            Post post = new Post();
            post.setImageUrl(postRequestDTO.getImageUrl());
            post.setTitle(postRequestDTO.getTitle());
            post.setCreateAt(LocalDate.now());
            post.setLocation(userAddress.getCity());
            post.setPostStatus(postRequestDTO.getStatus());
            post.setUser(user);
            post = postRepo.save(post);

            Subcategory subcategory = subcategoryRepo.getById(postRequestDTO.getSubCategoryId());

            List<BookRequestDTO> bookRequestDTOS = postRequestDTO.getBookList();
            for (BookRequestDTO bookRequestDTO: bookRequestDTOS) {
                Book book = new Book();
                book.setSubcategory(subcategory);
                book.setName(bookRequestDTO.getName());
                book.setIsbn(bookRequestDTO.getIsbn());
                book.setAuthor(bookRequestDTO.getAuthor());
                book.setPublicationDate(bookRequestDTO.getPublicationDate());
                book.setPublicCompany(bookRequestDTO.getPublicCompany());
                book.setLanguage(bookRequestDTO.getLanguage());
                book.setCoverType(bookRequestDTO.getCoverType());
                book.setStatusQuo(bookRequestDTO.getStatusQuo());
                book.setDescription(bookRequestDTO.getDescription());
                book.setInitPrice(bookRequestDTO.getInitPrice());
                book.setPrice(bookRequestDTO.getPrice());
                book.setPost(post);
                book = bookRepo.save(book);
                List<String> bookImages = bookRequestDTO.getBookImages();
                for (String bookImage:bookImages) {
                    BookImage _bookImage = new BookImage();
                    _bookImage.setUrl(bookImage);
                    _bookImage.setBook(book);
                    bookImageRepo.save(_bookImage);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
