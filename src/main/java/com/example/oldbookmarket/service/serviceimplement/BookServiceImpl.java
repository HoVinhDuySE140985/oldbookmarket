package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.bookDTO.UpdateBookResquestDTO;
import com.example.oldbookmarket.dto.response.bookDTO.BooKDetailResponseDTO;
import com.example.oldbookmarket.dto.response.bookDTO.BookImageResponseDTO;
import com.example.oldbookmarket.dto.response.bookDTO.BookResponseDTO;
import com.example.oldbookmarket.entity.*;
import com.example.oldbookmarket.repository.BookImageRepo;
import com.example.oldbookmarket.repository.BookRepo;
import com.example.oldbookmarket.repository.PostRepo;
import com.example.oldbookmarket.repository.UserRepo;
import com.example.oldbookmarket.repository.BookAuthorRepo;
import com.example.oldbookmarket.service.serviceinterface.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public List<BooKDetailResponseDTO> getBookInfo(Long postId) {
        List<BooKDetailResponseDTO> bookResponseDTOS = new ArrayList<>();
        List<Book> bookList = null;
        try {
            Post post = postRepo.findById(postId).get();
            User user = userRepo.findById(post.getUser().getId()).get();
            bookList = bookRepo.findAllByPost_Id(postId);
            for (Book book : bookList) {
                List<BookImage> bookImages = bookImageRepo.findAllByBook_Id(book.getId());
                BooKDetailResponseDTO bookResponseDTO = BooKDetailResponseDTO.builder()
                        .bookId(book.getId())
                        .name(book.getName())
                        .isbn(book.getIsbn())
                        .bookImages(bookImages)
                        .reprint(book.getReprints())
                        .publicationDate(book.getPublicationDate())
                        .publicCompany(book.getPublicCompany())
                        .author(book.getBookAuthor().getName())
                        .coverType(book.getCoverType())
                        .language(book.getLanguage())
                        .initPrice(post.getInitPrice())
                        .price(post.getPrice())
                        .bookExchange(post.getBookExchange())
                        .statusQuo(book.getStatusQuo())
                        .description(book.getDescription())
                        .subcategoryName(post.getSubcategory().getName())
                        .userId(user.getId())
                        .userName(user.getName())
                        .phoneNumber(user.getPhoneNumber())
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
        try {
            Book book = bookRepo.findById(updateBookResquestDTO.getBookId()).get();
            BookAuthor bookAuthor = bookAuthorRepo.findByName(updateBookResquestDTO.getAuthor());
            if (bookAuthor != null) {
                book.setName(updateBookResquestDTO.getName());
                book.setIsbn(updateBookResquestDTO.getIsbn());
                book.setReprints(updateBookResquestDTO.getReprints());
                book.setPublicationDate(updateBookResquestDTO.getPublicationDate());
                book.setPublicCompany(updateBookResquestDTO.getPublicCompany());
                book.setBookAuthor(bookAuthor);
                book.setCoverType(updateBookResquestDTO.getCoverType());
                book.setLanguage(updateBookResquestDTO.getLanguage());
                book.setStatusQuo(updateBookResquestDTO.getStatusQuo());
                book.setDescription(updateBookResquestDTO.getDescription());
                bookRepo.save(book);
                List<BookImage> bookImages = updateBookResquestDTO.getBookImages();
                for (BookImage bookImage : bookImages) {
                    BookImage image = bookImageRepo.findById(bookImage.getId()).get();
                    image.setUrl(bookImage.getUrl());
                    bookImageRepo.save(image);
                }
                Book _book = bookRepo.findById(book.getId()).get();
                List<BookImage> imageList = bookImageRepo.findAllByBook_Id(_book.getId());
                List<BookImageResponseDTO> bookImageResponseDTOS = new ArrayList<>();
                for (BookImage bookImage : imageList) {
                    BookImageResponseDTO bookImageResponseDTO = BookImageResponseDTO.builder()
                            .imageId(bookImage.getId())
                            .imageUrl(bookImage.getUrl())
                            .build();
                    bookImageResponseDTOS.add(bookImageResponseDTO);
                }
                bookResponseDTO = BookResponseDTO.builder()
                        .bookId(book.getId())
                        .name(book.getName())
                        .isbn(book.getIsbn())
                        .bookImages(bookImageResponseDTOS)
                        .publicationDate(book.getPublicationDate())
                        .publicCompany(book.getPublicCompany())
                        .author(book.getBookAuthor().getName())
                        .coverType(book.getCoverType())
                        .reprint(book.getReprints())
                        .language(book.getLanguage())
                        .statusQuo(book.getStatusQuo())
                        .description(book.getDescription())
                        .build();
            }
            if (bookAuthor == null){
                BookAuthor author = BookAuthor.builder()
                        .name(updateBookResquestDTO.getAuthor())
                        .build();
                author = bookAuthorRepo.save(author);
                book.setName(updateBookResquestDTO.getName());
                book.setIsbn(updateBookResquestDTO.getIsbn());
                book.setReprints(updateBookResquestDTO.getReprints());
                book.setPublicationDate(updateBookResquestDTO.getPublicationDate());
                book.setPublicCompany(updateBookResquestDTO.getPublicCompany());
                book.setBookAuthor(author);
                book.setCoverType(updateBookResquestDTO.getCoverType());
                book.setLanguage(updateBookResquestDTO.getLanguage());
                book.setStatusQuo(updateBookResquestDTO.getStatusQuo());
                book.setDescription(updateBookResquestDTO.getDescription());
                bookRepo.save(book);

                List<BookImage> bookImages = updateBookResquestDTO.getBookImages();
                System.out.println(updateBookResquestDTO.getBookImages());
                for (BookImage bookImage : bookImages) {
                    BookImage image = bookImageRepo.findById(bookImage.getId()).get();
                    image.setUrl(bookImage.getUrl());
                    bookImageRepo.save(image);
                }
                Book _book = bookRepo.findById(book.getId()).get();
                List<BookImage> imageList = bookImageRepo.findAllByBook_Id(_book.getId());
                List<BookImageResponseDTO> bookImageResponseDTOS = new ArrayList<>();
                for (BookImage bookImage : imageList) {
                    BookImageResponseDTO bookImageResponseDTO = BookImageResponseDTO.builder()
                            .imageId(bookImage.getId())
                            .imageUrl(bookImage.getUrl())
                            .build();
                    bookImageResponseDTOS.add(bookImageResponseDTO);
                }
                bookResponseDTO = BookResponseDTO.builder()
                        .bookId(book.getId())
                        .name(book.getName())
                        .isbn(book.getIsbn())
                        .bookImages(bookImageResponseDTOS)
                        .publicationDate(book.getPublicationDate())
                        .publicCompany(book.getPublicCompany())
                        .author(book.getBookAuthor().getName())
                        .coverType(book.getCoverType())
                        .reprint(book.getReprints())
                        .language(book.getLanguage())
                        .statusQuo(book.getStatusQuo())
                        .description(book.getDescription())
                        .build();
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return bookResponseDTO;
    }

//    @Override
//    public BookImageResponseDTO getAllImageOfBook(Long postId) {
//        BookImageResponseDTO bookImageResponseDTO = null;
//        List<Book> bookList = bookRepo.findAllByPost_Id(postId);
//        for (Book book : bookList) {
//            List<String> listImage = bookImageRepo.findAllByBook_Id(book.getId());
//            bookImageResponseDTO = BookImageResponseDTO.builder()
//                    .bookId(book.getId())
//                    .imageUrl(listImage)
//                    .build();
//        }
//        return bookImageResponseDTO;
//    }

    @Override
    public BookResponseDTO getBookById(Long bookId) {
        BookResponseDTO bookResponseDTO = null;
        try {
            Book book = bookRepo.findById(bookId).get();
            List<BookImageResponseDTO> bookImageResponseDTOS = new ArrayList<>();
            List<BookImage> bookImages = bookImageRepo.findAllByBook_Id(book.getId());

            for (BookImage bookImage : bookImages) {
                BookImageResponseDTO bookImageResponseDTO = BookImageResponseDTO.builder()
                        .imageId(bookImage.getId())
                        .imageUrl(bookImage.getUrl())
                        .build();
                bookImageResponseDTOS.add(bookImageResponseDTO);
            }
            bookResponseDTO = BookResponseDTO.builder()
                    .bookId(book.getId())
                    .name(book.getName())
                    .isbn(book.getIsbn())
                    .bookImages(bookImageResponseDTOS)
                    .publicationDate(book.getPublicationDate())
                    .publicCompany(book.getPublicCompany())
                    .author(book.getBookAuthor().getName())
                    .coverType(book.getCoverType())
                    .reprint(book.getReprints())
                    .language(book.getLanguage())
                    .statusQuo(book.getStatusQuo())
                    .description(book.getDescription())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookResponseDTO;
    }
}
