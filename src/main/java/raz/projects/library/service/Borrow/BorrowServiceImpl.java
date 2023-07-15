package raz.projects.library.service.Borrow;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import raz.projects.library.dto.pages.BorrowPageDto;
import raz.projects.library.dto.request.BorrowRequestDto;
import raz.projects.library.dto.response.BorrowResponseDto;
import raz.projects.library.dto.update.BorrowExtraTime;
import raz.projects.library.entity.Borrow;
import raz.projects.library.errors.BadRequestException;
import raz.projects.library.errors.ResourceNotFoundException;
import raz.projects.library.repository.BookRepository;
import raz.projects.library.repository.BorrowRepository;
import raz.projects.library.repository.CustomerRepository;
import raz.projects.library.repository.LibrarianRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;
    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;
    private final LibrarianRepository librarianRepository;
    private final ModelMapper mapper;

    @Override
    public List<BorrowResponseDto> getBorrowed() {

        return borrowRepository.findAll()
                .stream()
                .map(borrow -> mapper.map(borrow, BorrowResponseDto.class))
                .toList();
    }

    @Override
    public BorrowPageDto getBorrowedPage(
            int pageNo, int pageSize, String sortBy, String sortDir,
            String customerId, String bookId, String addedBy, Boolean returnBook,
            String borrowingDateStart, String borrowingDateEnd,
            String returnDateStart, String returnDateEnd) {


        Specification<Borrow> specification = Specification.where(null);


        if (customerId != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("customerId"), customerId));
        }

        if (bookId != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("bookId"), bookId));
        }

        if (addedBy != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("addedBy"), addedBy));
        }

        if (returnBook != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("returnBook"), returnBook));
        }

        if (borrowingDateStart != null && borrowingDateEnd != null) {
            LocalDate startDate = LocalDate.parse(borrowingDateStart);
            LocalDate endDate = LocalDate.parse(borrowingDateEnd);
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("borrowingDate"), startDate, endDate));
        }

        if (returnDateStart != null && returnDateEnd != null) {
            LocalDate startDate = LocalDate.parse(returnDateStart);
            LocalDate endDate = LocalDate.parse(returnDateEnd);
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("returnDate"), startDate, endDate));
        }


        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.fromString(sortDir), sortBy);

        Page<Borrow> page = borrowRepository.findAll(specification ,pageable);

        return BorrowPageDto.builder()
                .results(page.stream().map(borrow -> mapper.map(borrow, BorrowResponseDto.class)).toList())
                .totalPages(page.getTotalPages())
                .totalBorrowed(page.getTotalElements())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .build();
    }

    @Override
    public BorrowResponseDto addBorrow(BorrowRequestDto dto, Authentication authentication) {

        var customer = customerRepository.findById(dto.getCustomerId()).orElseThrow(
                () -> new BadRequestException(
                        "add borrow - get customer" ,dto.getCustomerId(), "This customer doesn't exist in the library")
        );

        if (!customer.isActive()) {
            throw new BadRequestException(
                    "add borrow",
                    dto.getCustomerId(),
                    "It is not possible to lend a book to an inactive customer");
        }

        if (customer.getCustomerType() == null) {
            throw new BadRequestException(
                    "add borrow",
                    dto.getCustomerId(),
                    "It is not possible to lend a book to a customer without a type");
        }

        List<Borrow> allBorrowsCustomer =
                borrowRepository.findAllBorrowByCustomer_IdAndReturnBookIsFalse(dto.getCustomerId());

        int currentQuantityCustomer = allBorrowsCustomer.size();
        int maxQuantity = customer.getCustomerType().getAmount();

        if (currentQuantityCustomer >= maxQuantity) {
            throw new BadRequestException(
                    "add borrow", dto.getCustomerId(), "The customer has reached the limit of books");
        }

        var book = bookRepository.findById(dto.getBookId()).orElseThrow(
                () -> new BadRequestException(
                        "add borrow - get book" ,dto.getBookId(), "This book doesn't exist in the library")
                );

        List<Borrow> allBorrowsBook =
                borrowRepository.findAllBorrowByBook_IdAndReturnBookIsFalse(dto.getBookId());

        int currentQuantityBook = allBorrowsBook.size();

        if (currentQuantityBook >= 1) {
            throw new BadRequestException(
                    "add borrow", dto.getBookId(), "This book is already borrowed");
        }

        var librarian = librarianRepository.findLibrarianByUserNameIgnoreCase(authentication.getName()).orElseThrow();

        var borrow = Borrow.builder()
                .customer(customer)
                .book(book)
                .borrowingDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(customer.getCustomerType().getDays()))
                .returnBook(false)
                .addedBy(librarian)
                .build();

        borrowRepository.save(borrow);
        var response = mapper.map(borrow, BorrowResponseDto.class);

        response.setCustomerId(borrow.getCustomer().getId());
        response.setBookId(borrow.getBook().getId());
        response.setAddedByUserName(borrow.getAddedBy().getUserName());

        return response;

    }

    @Override
    public BorrowResponseDto getBorrowById(Long id) {

        var borrow = borrowRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("get borrow", id, "There is no borrowing such a book")
        );

        return mapper.map(borrow, BorrowResponseDto.class);

    }

    @Override
    public BorrowResponseDto extraTime(BorrowExtraTime dto, Long id) {

        var borrow = borrowRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("extra time", id, "There is no borrowing such a book")
        );

        if (borrow.isReturnBook()) {
            throw new BadRequestException(
                    "extra time", id, "This book has already been returned");
        }

        else
            borrow.setReturnDate(borrow.getReturnDate().plusDays(dto.getDays()));

        var save = borrowRepository.save(borrow);

        return mapper.map(save, BorrowResponseDto.class);

    }

    @Override
    public BorrowResponseDto returnBook(Long id) {

        var borrow = borrowRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("return book", id, "There is no borrowing such a book")
        );

        if (borrow.isReturnBook()) {
            throw new BadRequestException(
                    "return book", id, "This book has already been returned");
        }

        else {
            borrow.setReturnBook(true);
            borrow.setRetrievedOn(LocalDate.now());
        }

        var save = borrowRepository.save(borrow);

        return mapper.map(save, BorrowResponseDto.class);

    }
}
