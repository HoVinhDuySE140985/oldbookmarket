package com.example.oldbookmarket.service.serviceimplement;

import com.example.oldbookmarket.dto.request.NotiRequestDTO.PnsRequest;
import com.example.oldbookmarket.dto.request.bookDTO.BookRequestDTO;
import com.example.oldbookmarket.dto.request.postDTO.PostRequestDTO;
import com.example.oldbookmarket.dto.response.bookDTO.BookPendingResponseDTO;
import com.example.oldbookmarket.dto.response.bookauthorDTO.BookAuthorResponseDTO;
import com.example.oldbookmarket.dto.response.postDTO.PostResponseDTO;
import com.example.oldbookmarket.entity.*;
import com.example.oldbookmarket.repository.*;
import com.example.oldbookmarket.repository.BookAuthorRepo;
import com.example.oldbookmarket.service.serviceinterface.FcmService;
import com.example.oldbookmarket.service.serviceinterface.PostService;
import com.example.oldbookmarket.shared.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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

    @Autowired
    PostNotificationRepo postNotificationRepo;

    @Autowired
    FcmService fcmService;

    @Autowired
    WalletRepo walletRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    TransactionRepo transactionRepo;

    @Override
    public PostResponseDTO createPost(PostRequestDTO postRequestDTO) {
        PostResponseDTO postResponseDTO = null;
        User user = userRepo.findById(postRequestDTO.getUserId()).get();
        Subcategory subcategory = null;
        Transaction transaction = null;
        BookAuthor bookAuthor = null;
        String orderCode = Utilities.randomAlphaNumeric(10);
        try {
            int postAmount = postRepo.findAllPost(postRequestDTO.getUserId());
            if (postAmount <= 3) {
                // 3 bài đăng đầu đc free
                subcategory = subcategoryRepo.findById(postRequestDTO.getSubCategoryId()).get();
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
                post.setIsCheck(0);
                post.setPostStatus("pending");
                post.setUser(user);
                post = postRepo.save(post);

                List<BookRequestDTO> bookRequestDTOS = postRequestDTO.getBookList();
                for (BookRequestDTO bookRequestDTO : bookRequestDTOS) {

                    List<String> listAuthor = bookRequestDTO.getAuthor();
                    for (String authorBook : listAuthor) {
                        BookAuthor author = bookAuthorRepo.findByName(authorBook);
                        if (author != null) {
                            Book book = new Book();
                            book.setName(bookRequestDTO.getName());
                            book.setIsbn(bookRequestDTO.getIsbn());
                            book.setReprints(bookRequestDTO.getReprints());
                            book.setBookAuthor(author);
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
                        } else {
                            BookAuthor _author = new BookAuthor();
                            _author.setName(authorBook);
                            bookAuthorRepo.save(_author);
                            Book book = new Book();
                            book.setName(bookRequestDTO.getName());
                            book.setIsbn(bookRequestDTO.getIsbn());
                            book.setBookAuthor(_author);
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
                    }
                    postResponseDTO = PostResponseDTO.builder().id(post.getId()).title(post.getTitle())
                            .imageUrl(post.getImageUrl()).form(post.getForm()).price(post.getPrice())
                            .location(post.getLocation()).userId(post.getUser().getId()).status(post.getPostStatus())
                            .build();
                }
            } else {
                // từ 3 bài đăng trờ đi mất phí 14k duy trì 7 ngày
                Wallet walletPoster = walletRepo.findById(user.getId()).get();
                if (walletPoster.getAmount().compareTo(BigDecimal.valueOf(14000)) == -1) {
                    throw new ResponseStatusException(HttpStatus.valueOf(400), "Ví Bạn Không Đủ Tiền Vui Lòng Nạp Thêm Và Thử Lại");
                } else {
                    walletPoster.setAmount(walletPoster.getAmount().subtract(BigDecimal.valueOf(14000)));
                    walletRepo.save(walletPoster);
                    transaction = Transaction.builder()
                            .createAt(LocalDate.now())
                            .type("Gia Hạn Thời Gian Bài Đăng")
                            .paymentMethod("Ví Của Tôi")
                            .orderCode(orderCode)
                            .wallet(walletPoster)
                            .amount(BigDecimal.valueOf(14000))
                            .build();
                    transactionRepo.save(transaction);
                    User admin = userRepo.findUserByRole_Id(1L);
                    Wallet walletAdmin = walletRepo.findByUserId(admin.getId());
                    walletAdmin.setAmount(walletAdmin.getAmount().add(BigDecimal.valueOf(14000)));
                    walletRepo.save(walletAdmin);
                    transaction = Transaction.builder()
                            .createAt(LocalDate.now())
                            .type("Nhận Tiền Gia Hạn ")
                            .paymentMethod("Ví Của Tôi")
                            .orderCode(orderCode)
                            .wallet(walletAdmin)
                            .amount(BigDecimal.valueOf(14000))
                            .build();
                    transactionRepo.save(transaction);
                    subcategory = subcategoryRepo.findById(postRequestDTO.getSubCategoryId()).get();
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
                    post.setExpDate(LocalDate.now().plusDays(7));
                    post.setPostStatus("pending");
                    post.setUser(user);
                    post = postRepo.save(post);

                    List<BookRequestDTO> bookRequestDTOS = postRequestDTO.getBookList();
                    for (BookRequestDTO bookRequestDTO : bookRequestDTOS) {
                        List<String> listAuthor = bookRequestDTO.getAuthor();
                        for (String author : listAuthor) {
                            bookAuthor = bookAuthorRepo.findByName(author);
                            if (bookAuthor != null) {
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
                            } else {
                                BookAuthor _author = BookAuthor.builder()
                                        .name(author)
                                        .build();
                                bookAuthorRepo.save(_author);
                                Book book = new Book();
                                book.setName(bookRequestDTO.getName());
                                book.setIsbn(bookRequestDTO.getIsbn());
                                book.setBookAuthor(_author);
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
                        }
                        postResponseDTO = PostResponseDTO.builder().id(post.getId()).title(post.getTitle())
                                .imageUrl(post.getImageUrl()).form(post.getForm()).price(post.getPrice())
                                .location(post.getLocation()).userId(post.getUser().getId()).status(post.getPostStatus())
                                .build();
                    }
                }

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
                if (post.getUser().getEmail().equals(email)) {
                    PostResponseDTO postResponseDTO = new PostResponseDTO();
                    postResponseDTO.setId(post.getId());
                    postResponseDTO.setTitle(post.getTitle());
                    postResponseDTO.setForm(post.getForm());
                    postResponseDTO.setImageUrl(post.getImageUrl());
                    postResponseDTO.setLocation(post.getLocation());
                    postResponseDTO.setInitPrice(post.getInitPrice());
                    postResponseDTO.setExpDate(post.getExpDate());
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
                    BookPendingResponseDTO responseDTO = BookPendingResponseDTO.builder().bookId(book.getId())
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
                            .imageBook(book.getImageList()).build();
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
            // nếu key null và sort null
            if (keyWord.equalsIgnoreCase("null") && sortBy.equalsIgnoreCase("null")) {
                postList = postRepo.findAll();
            }
            // nếu key null và sort tăng
            if (keyWord.equalsIgnoreCase("null") && sortBy.equalsIgnoreCase("tăng dần")) {
                postList = postRepo.findAll(Sort.by("price").ascending());
            }
            // nếu key null và sort giảm
            if (keyWord.equalsIgnoreCase("null") && sortBy.equalsIgnoreCase("giảm dần")) {
                postList = postRepo.findAll(Sort.by("price").descending());
            }
            // nếu key != null và sort null
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
                    System.out.println(postList.size());
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
                            BookPendingResponseDTO responseDTO = BookPendingResponseDTO.builder().bookId(book.getId())
                                    .name(book.getName()).coverType(book.getCoverType())
                                    .description(book.getDescription()).isbn(book.getIsbn())
                                    .publicationDate(book.getPublicationDate()).bookExchange(post.getBookExchange())
                                    .publicCompany(book.getPublicCompany()).statusQuo(book.getStatusQuo())
                                    .language(book.getLanguage()).author(book.getBookAuthor().getName())
                                    .imageBook(book.getImageList()).build();
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
                            BookPendingResponseDTO responseDTO = BookPendingResponseDTO.builder().bookId(book.getId())
                                    .name(book.getName()).coverType(book.getCoverType())
                                    .description(book.getDescription()).isbn(book.getIsbn())
                                    .publicationDate(book.getPublicationDate()).bookExchange(post.getBookExchange())
                                    .publicCompany(book.getPublicCompany()).statusQuo(book.getStatusQuo())
                                    .language(book.getLanguage()).author(book.getBookAuthor().getName())
                                    .imageBook(book.getImageList()).build();
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
            if(post.getIsCheck() == 0){
                post.setPostStatus("active");
                post.setIsCheck(1);
                post.setCreateAt(LocalDate.now());
                post.setExpDate(LocalDate.now().plusDays(7));
            }
            if (post.getIsCheck()==1){
                post.setPostStatus("active");
            }
            postRepo.save(post);
            postResponseDTO.setId(post.getId());
            postResponseDTO.setTitle(post.getTitle());
            postResponseDTO.setForm(post.getForm());
            postResponseDTO.setImageUrl(post.getImageUrl());
            postResponseDTO.setLocation(post.getLocation());
            postResponseDTO.setPrice(post.getPrice());
            postResponseDTO.setStatus(post.getPostStatus());
            postResponseDTO.setUserId(post.getUser().getId());
            // gửi noti cho người đăng
            List<String> fcmKey = new ArrayList<>();
            User user = userRepo.findUserByEmail(post.getUser().getEmail());
            if (!user.getFcmKey().isEmpty() && user.getFcmKey() != null) {
                fcmKey.add(user.getFcmKey());
            }
            if (!fcmKey.isEmpty() || fcmKey.size() > 0) { // co key
                // pushnoti
                PnsRequest pnsRequest = new PnsRequest(fcmKey, "Sách Đăng Ký",
                        "Cuốn sách bạn đang tìm đã xuất hiện ");
                fcmService.pushNotification(pnsRequest);
            }
            // gửi noti cho người đăng ký nhận thông tin
            List<String> fcmKey1 = new ArrayList<>();
            User poster = userRepo.findUserByEmail(post.getUser().getEmail());
            if (!poster.getFcmKey().isEmpty() && poster.getFcmKey() != null) {
                fcmKey1.add(poster.getFcmKey());
            }
            if (!fcmKey1.isEmpty() || fcmKey1.size() > 0) { // co key
                // pushnoti
                PnsRequest pnsRequest = new PnsRequest(fcmKey1, "Sách Đăng Ký",
                        "Cuốn sách bạn đang tìm đã được đăng ");
                fcmService.pushNotification(pnsRequest);
            }

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
            postResponseDTO.setId(post.getId());
            postResponseDTO.setTitle(post.getTitle());
            postResponseDTO.setForm(post.getForm());
            postResponseDTO.setImageUrl(post.getImageUrl());
            postResponseDTO.setPrice(post.getPrice());
            postResponseDTO.setLocation(post.getLocation());
            postResponseDTO.setStatus(post.getPostStatus());
            postResponseDTO.setReasonReject(post.getReasonReject());
            postResponseDTO.setUserId(post.getUser().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postResponseDTO;
    }

    @Override
    public PostResponseDTO updatePostStatus(Long id) {
        PostResponseDTO postResponseDTO = new PostResponseDTO();
        Post post = postRepo.findById(id).get();
        Order order = orderRepo.findOrderByPostId(id);
        if (order == null) {
            if (post.getPostStatus().equalsIgnoreCase("active")) {
                post.setPostStatus("deactive");
                postRepo.save(post);
            } else if (post.getPostStatus().equalsIgnoreCase("deactive")) {
                post.setPostStatus("active");
                postRepo.save(post);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.valueOf(400), "bài đăng đã được mua/trao đổi, không thể cập nhập trạng thái");
        }
        postResponseDTO.setId(post.getId());
        postResponseDTO.setTitle(post.getTitle());
        postResponseDTO.setForm(post.getForm());
        postResponseDTO.setImageUrl(post.getImageUrl());
        postResponseDTO.setLocation(post.getLocation());
        postResponseDTO.setPrice(post.getPrice());
        postResponseDTO.setStatus(post.getPostStatus());
        postResponseDTO.setUserId(post.getUser().getId());

        return postResponseDTO;
    }

    @Override
    public PostResponseDTO updatePostInfo(PostRequestDTO postRequestDTO) {
        PostResponseDTO postResponseDTO = null;
        try {
            Post post = postRepo.findById(postRequestDTO.getId()).get();
            Order order = orderRepo.findOrderByPostId(post.getId());
            if (order == null) {
                post.setPostStatus("pending");
                post.setTitle(postRequestDTO.getTitle());
                post.setImageUrl(postRequestDTO.getImageUrl());
                post.setLocation(postRequestDTO.getLocation());
                post.setPrice(postRequestDTO.getPrice());
                post.setInitPrice(postRequestDTO.getInitPrice());
                post.setBookExchange(postRequestDTO.getBookExchange());
                post = postRepo.save(post);
                postResponseDTO = PostResponseDTO.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .form(post.getForm())
                        .imageUrl(post.getImageUrl())
                        .initPrice(post.getInitPrice())
                        .price(post.getPrice())
                        .bookExchange(post.getBookExchange())
                        .location(post.getLocation())
                        .build();
            } else {
                throw new ResponseStatusException(HttpStatus.valueOf(400), "Bài đang đã được mua/trao đổi không thể cập nhập thông tin");
            }

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
                    if (post.getPostStatus().equalsIgnoreCase("active")
                            && post.getLocation().equalsIgnoreCase(filter)) {
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
                            BookPendingResponseDTO responseDTO = BookPendingResponseDTO.builder().bookId(book.getId())
                                    .name(book.getName()).coverType(book.getCoverType())
                                    .description(book.getDescription()).isbn(book.getIsbn())
                                    .publicationDate(book.getPublicationDate()).bookExchange(post.getBookExchange())
                                    .publicCompany(book.getPublicCompany()).statusQuo(book.getStatusQuo())
                                    .language(book.getLanguage())
                                    .author(book.getBookAuthor().getName())
                                    .imageBook(book.getImageList()).build();
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
                            BookPendingResponseDTO responseDTO = BookPendingResponseDTO.builder().bookId(book.getId())
                                    .name(book.getName()).coverType(book.getCoverType())
                                    .description(book.getDescription()).isbn(book.getIsbn())
                                    .publicationDate(book.getPublicationDate()).bookExchange(post.getBookExchange())
                                    .publicCompany(book.getPublicCompany()).statusQuo(book.getStatusQuo())
                                    .language(book.getLanguage())
                                    .author(book.getBookAuthor().getName())
                                    .imageBook(book.getImageList()).build();
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
    public List<PostResponseDTO> getAllNewPost() {
        List<Post> postList = new ArrayList<>();
        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();
        try {
            postList = postRepo.findTop10ByPostStatusOrderByCreateAtDesc("active");
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
                    BookPendingResponseDTO responseDTO = BookPendingResponseDTO.builder().bookId(book.getId())
                            .name(book.getName()).coverType(book.getCoverType())
                            .description(book.getDescription())
                            .isbn(book.getIsbn()).reprints(book.getReprints())
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
    public PostResponseDTO getPostById(Long postId) {
        PostResponseDTO postResponseDTO = null;
        try {
            Post post = postRepo.findById(postId).get();
            List<Book> bookList = post.getBooks();
            List<BookPendingResponseDTO> bookPendingResponseDTOS = new ArrayList<>();
            for (Book book : bookList) {
                BookPendingResponseDTO responseDTO = BookPendingResponseDTO.builder().bookId(book.getId())
                        .name(book.getName()).coverType(book.getCoverType()).description(book.getDescription())
                        .isbn(book.getIsbn()).publicationDate(book.getPublicationDate())
                        .bookExchange(post.getBookExchange()).publicCompany(book.getPublicCompany())
                        .statusQuo(book.getStatusQuo()).language(book.getLanguage())
                        .author(book.getBookAuthor().getName()).imageBook(book.getImageList()).build();
                bookPendingResponseDTOS.add(responseDTO);
            }
            postResponseDTO = PostResponseDTO.builder().id(post.getId()).title(post.getTitle()).form(post.getForm())
                    .imageUrl(post.getImageUrl()).location(post.getLocation()).bookList(bookPendingResponseDTOS)
                    .initPrice(post.getInitPrice()).price(post.getPrice()).bookExchange(post.getBookExchange()).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postResponseDTO;
    }


    @Override
    public Boolean postExtension(Long postId) {
        Wallet wallet = null;
        Post post = postRepo.findById(postId).get();
        if (post.getPostStatus().equalsIgnoreCase("expired")) {
            wallet = walletRepo.findByUserId(post.getUser().getId());
            if (wallet.getAmount().compareTo(BigDecimal.valueOf(14000)) < 0) {
                throw new ResponseStatusException(HttpStatus.valueOf(400), "Tiền trong ví không đủ vui lòng nạp thêm");
            } else {
                // trừ tiền ví người đăng bài
                wallet.setAmount(wallet.getAmount().subtract(BigDecimal.valueOf(14000)));
                walletRepo.save(wallet);
                // cộng tiền vô ví admin
                User user = userRepo.findUserByRole_Id(1L);
                wallet = walletRepo.findById(user.getId()).get();
                wallet.setAmount(wallet.getAmount().add(BigDecimal.valueOf(14000)));
                walletRepo.save(wallet);
            }
            post.setPostStatus("active");
            post.setExpDate(LocalDate.now().plusDays(7));
            postRepo.save(post);
            List<String> fcmKey1 = new ArrayList<>();
            if (!post.getUser().getFcmKey().isEmpty() && post.getUser().getFcmKey() != null) {
                fcmKey1.add(post.getUser().getFcmKey());
            }
            if (!fcmKey1.isEmpty() || fcmKey1.size() > 0) { // co key
                // pushnoti
                PnsRequest pnsRequest = new PnsRequest(fcmKey1, "Gia hạn thành Công",
                        "Bài đăng của bạn đã được gia hạn thành công :");
                fcmService.pushNotification(pnsRequest);
            }
            return true;
        }
        return false;
    }
}
