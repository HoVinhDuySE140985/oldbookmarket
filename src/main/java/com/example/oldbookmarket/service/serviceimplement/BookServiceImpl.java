package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.bookDTO.UpdateBookResquestDTO;
import com.example.oldbookmarket.dto.respone.BookImageResponseDTO;
import com.example.oldbookmarket.dto.respone.BookResponseDTO;
import com.example.oldbookmarket.entity.Book;
import com.example.oldbookmarket.entity.Post;
import com.example.oldbookmarket.repository.BookImageRepo;
import com.example.oldbookmarket.repository.BookRepo;
import com.example.oldbookmarket.repository.PostRepo;
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
    BookImageRepo bookImageRepo;

    @Override
    public BookResponseDTO getBookInfor(Long postId, Long bookId) {
        BookResponseDTO bookResponseDTO = null;
        List<Book> bookList = null;
        try {
            bookList = bookRepo.findAllByPost_Id(postId);
            Book  book = new Book();
            if(bookId == null){
                book = bookRepo.getFirstByPost_Id(postId);
                bookResponseDTO = BookResponseDTO.builder()
                        .name(book.getName())
                        .isbn(book.getIsbn())
                        .bookImages(book.getImageList())
                        .publicationDate(book.getPublicationDate())
                        .publicCompany(book.getPublicCompany())
                        .author(book.getAuthor())
                        .coverType(book.getCoverType())
                        .language(book.getLanguage())
                        .statusQuo(book.getStatusQuo())
//                        .description(book.getDescription())
                        .build();
            }else {
                for (Book books: bookList) {
                    if(books.getId().equals(bookId)){
                        book = bookRepo.getById(bookId);
                        bookResponseDTO = BookResponseDTO.builder()
                                .name(book.getName())
                                .isbn(book.getIsbn())
                                .bookImages(book.getImageList())
                                .publicationDate(book.getPublicationDate())
                                .publicCompany(book.getPublicCompany())
                                .author(book.getAuthor())
                                .coverType(book.getCoverType())
                                .language(book.getLanguage())
                                .statusQuo(book.getStatusQuo())
//                                .description(book.getDescription())
                                .build();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return bookResponseDTO;
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
            for (Book books: bookList) {
                if(books.getId().equals(updateBookResquestDTO.getBookId())){
                    Book book = bookRepo.getById(updateBookResquestDTO.getBookId());
                    book.setName(updateBookResquestDTO.getName());
                    book.setIsbn(updateBookResquestDTO.getIsbn());
                    book.setImageList(updateBookResquestDTO.getBookImages());
                    book.setPublicationDate(updateBookResquestDTO.getPublicationDate());
                    book.setPublicCompany(updateBookResquestDTO.getPublicCompany());
                    book.setAuthor(updateBookResquestDTO.getAuthor());
                    book.setCoverType(updateBookResquestDTO.getCoverType());
                    book.setLanguage(updateBookResquestDTO.getLanguage());
                    book.setStatusQuo(updateBookResquestDTO.getStatusQuo());
//                    book.setDescription(updateBookResquestDTO.getDescription());
                    bookRepo.save(book);
                    bookResponseDTO = BookResponseDTO.builder()
                            .name(book.getName())
                            .isbn(book.getIsbn())
                            .bookImages(book.getImageList())
                            .publicationDate(book.getPublicationDate())
                            .publicCompany(book.getPublicCompany())
                            .author(book.getAuthor())
                            .coverType(book.getCoverType())
                            .language(book.getLanguage())
                            .statusQuo(book.getStatusQuo())
//                            .description(book.getDescription())
                            .build();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return bookResponseDTO;
    }

    @Override
    public BookImageResponseDTO getAllImageOfBook(Long postId) {
        BookImageResponseDTO bookImageResponseDTO = null;
        List<Book> bookList = bookRepo.findAllByPost_Id(postId);
        for (Book book: bookList) {
            List<String> listImage = bookImageRepo.findAllByBook_Id(book.getId());
            bookImageResponseDTO = BookImageResponseDTO.builder()
                    .bookId(book.getId())
                    .imageUrl(listImage)
                    .build();
        }
        return bookImageResponseDTO;
    }
}
