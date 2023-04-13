package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.bookDTO.UpdateBookResquestDTO;
import com.example.oldbookmarket.dto.response.bookDTO.BookImageResponseDTO;
import com.example.oldbookmarket.dto.response.bookDTO.BookResponseDTO;
import com.example.oldbookmarket.entity.Book;
import com.example.oldbookmarket.entity.BookAuthor;
import com.example.oldbookmarket.entity.Post;
import com.example.oldbookmarket.entity.User;
import com.example.oldbookmarket.repository.BookImageRepo;
import com.example.oldbookmarket.repository.BookRepo;
import com.example.oldbookmarket.repository.PostRepo;
import com.example.oldbookmarket.repository.UserRepo;
import com.example.oldbookmarket.service.serviceinterface.BookAuthorRepo;
import com.example.oldbookmarket.service.serviceinterface.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepo bookRepo;

    @Autowired
    PostRepo postRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    BookImageRepo bookImageRepo;

    @Autowired
    BookAuthorRepo bookAuthorRepo;

    @Override
    public List<BookResponseDTO> getBookInfor(Long postId) {
        List<BookResponseDTO> bookResponseDTOS = new ArrayList<>();
        List<Book> bookList = null;
        try {
            Post post = postRepo.findById(postId).get();
            User user = userRepo.findById(post.getUser().getId()).get();
            bookList = bookRepo.findAllByPost_Id(postId);
            for (Book book : bookList) {
                BookResponseDTO bookResponseDTO = BookResponseDTO.builder()
                        .name(book.getName())
                        .isbn(book.getIsbn())
                        .bookImages(book.getImageList())
                        .reprint(book.getReprints())
                        .publicationDate(book.getPublicationDate())
                        .publicCompany(book.getPublicCompany())
                        .author(book.getBookAuthor().getName())
                        .coverType(book.getCoverType())
                        .language(book.getLanguage())
                        .price(post.getPrice())
                        .bookExchange(post.getBookExchange())
                        .statusQuo(book.getStatusQuo())
                        .description(book.getDescription())
                        .subcategoryName(post.getSubcategory().getName())
                        .userId(user.getId())
                        .userName(user.getName())
                        .createAt(post.getCreateAt())
                        .build();
                bookResponseDTOS.add(bookResponseDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookResponseDTOS;
    }

    @Override
    public BookResponseDTO updateBookInfo(UpdateBookResquestDTO updateBookResquestDTO) {
        BookResponseDTO bookResponseDTO = null;
        List<Book> bookList = new ArrayList<>();
        try {
            Post post = postRepo.getById(updateBookResquestDTO.getPostId());
            post.setPostStatus("pending");
            postRepo.save(post);

            bookList = bookRepo.findAllByPost_Id(updateBookResquestDTO.getPostId());
            for (Book books : bookList) {
                if (books.getId().equals(updateBookResquestDTO.getBookId())) {
                    Book book = bookRepo.getById(updateBookResquestDTO.getBookId());
                    BookAuthor bookAuthor = bookAuthorRepo.findByName(updateBookResquestDTO.getAuthor());
                    if (bookAuthor != null) {
                        book.setName(updateBookResquestDTO.getName());
                        book.setIsbn(updateBookResquestDTO.getIsbn());
                        book.setImageList(updateBookResquestDTO.getBookImages());
                        book.setPublicationDate(updateBookResquestDTO.getPublicationDate());
                        book.setPublicCompany(updateBookResquestDTO.getPublicCompany());
                        book.setBookAuthor(bookAuthor);
                        book.setCoverType(updateBookResquestDTO.getCoverType());
                        book.setLanguage(updateBookResquestDTO.getLanguage());
                        book.setStatusQuo(updateBookResquestDTO.getStatusQuo());
                        book.setDescription(updateBookResquestDTO.getDescription());
                        bookRepo.save(book);
                        bookResponseDTO = BookResponseDTO.builder()
                                .name(book.getName())
                                .isbn(book.getIsbn())
                                .bookImages(book.getImageList())
                                .publicationDate(book.getPublicationDate())
                                .publicCompany(book.getPublicCompany())
                                .author(book.getBookAuthor().getName())
                                .coverType(book.getCoverType())
                                .language(book.getLanguage())
                                .statusQuo(book.getStatusQuo())
                                .description(book.getDescription())
                                .build();
                    } else {
                        BookAuthor author = BookAuthor.builder()
                                .name(updateBookResquestDTO.getAuthor())
                                .build();
                        author = bookAuthorRepo.save(author);
                        book.setName(updateBookResquestDTO.getName());
                        book.setIsbn(updateBookResquestDTO.getIsbn());
                        book.setImageList(updateBookResquestDTO.getBookImages());
                        book.setPublicationDate(updateBookResquestDTO.getPublicationDate());
                        book.setPublicCompany(updateBookResquestDTO.getPublicCompany());
                        book.setBookAuthor(author);
                        book.setCoverType(updateBookResquestDTO.getCoverType());
                        book.setLanguage(updateBookResquestDTO.getLanguage());
                        book.setStatusQuo(updateBookResquestDTO.getStatusQuo());
                        book.setDescription(updateBookResquestDTO.getDescription());
                        bookRepo.save(book);
                        bookResponseDTO = BookResponseDTO.builder()
                                .name(book.getName())
                                .isbn(book.getIsbn())
                                .bookImages(book.getImageList())
                                .publicationDate(book.getPublicationDate())
                                .publicCompany(book.getPublicCompany())
                                .author(book.getBookAuthor().getName())
                                .coverType(book.getCoverType())
                                .language(book.getLanguage())
                                .statusQuo(book.getStatusQuo())
                                .description(book.getDescription())
                                .build();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookResponseDTO;
    }

    @Override
    public BookImageResponseDTO getAllImageOfBook(Long postId) {
        BookImageResponseDTO bookImageResponseDTO = null;
        List<Book> bookList = bookRepo.findAllByPost_Id(postId);
        for (Book book : bookList) {
            List<String> listImage = bookImageRepo.findAllByBook_Id(book.getId());
            bookImageResponseDTO = BookImageResponseDTO.builder()
                    .bookId(book.getId())
                    .imageUrl(listImage)
                    .build();
        }
        return bookImageResponseDTO;
    }
}
