package raz.projects.library.service.Book;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raz.projects.library.dto.pages.BookCategoriesPageDto;
import raz.projects.library.dto.request.BookCategoriesRequestDto;
import raz.projects.library.dto.response.BookCategoriesResponseDto;
import raz.projects.library.entity.Book;
import raz.projects.library.entity.BookCategories;
import raz.projects.library.errors.BadRequestException;
import raz.projects.library.errors.ResourceNotFoundException;
import raz.projects.library.repository.BookCategoriesRepository;
import raz.projects.library.repository.BookRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCategoriesServiceImpl implements BookCategoriesService {

    private final BookCategoriesRepository bookCategoriesRepository;
    private final BookRepository bookRepository;
    private final ModelMapper mapper;
    @Override
    public List<BookCategoriesResponseDto> getCategories() {

        return bookCategoriesRepository.findAll()
                .stream()
                .map(category -> mapper.map(category, BookCategoriesResponseDto.class))
                .toList();
    }

    @Override
    public BookCategoriesPageDto getCategoriesPage(
            int pageNo, int pageSize, String sortBy, String sortDir, String name) {

        Specification<BookCategories> specification = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(
                            root.get("name")), "%" + name.toLowerCase() + "%" ));
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.fromString(sortDir), sortBy);

        Page<BookCategories> page = bookCategoriesRepository.findAll(specification ,pageable);

        return BookCategoriesPageDto.builder()
                .results(page.stream().map(category -> mapper.map(category, BookCategoriesResponseDto.class)).toList())
                .totalPages(page.getTotalPages())
                .totalCategories(page.getTotalElements())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .build();
    }

    @Override
    public BookCategoriesResponseDto addCategories(BookCategoriesRequestDto dto) {

        var category = mapper.map(dto, BookCategories.class);

        try {
            bookCategoriesRepository.save(category);
            return mapper.map(category, BookCategoriesResponseDto.class);
        }
        catch (DataIntegrityViolationException exception) {
            throw new BadRequestException(
                    "add book -category ", category.getId(), "This book - category is already in the library");
        }
    }

    @Override
    public BookCategoriesResponseDto getCategoryById(long id) {

        var category = bookCategoriesRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "get book - category" ,id, "This book - category doesn't exist in the library")
        );

        return mapper.map(category, BookCategoriesResponseDto.class);
    }

    @Override
    public BookCategoriesResponseDto updateCategory(BookCategoriesRequestDto dto, Long id) {

        var category = bookCategoriesRepository.findById(id).orElseThrow(
                () -> new BadRequestException(
                        "update book - category", id, "This book - category doesn't exist in the library")
        );

        category.setName(dto.getName());

        try {
            bookCategoriesRepository.save(category);
            return mapper.map(category, BookCategoriesResponseDto.class);
        }
        catch (DataIntegrityViolationException exception) {
            throw new BadRequestException(
                    "add book -category ", category.getId(), "This book - category is already in the library");
        }
    }

    @Override
    public BookCategoriesResponseDto deleteCategoryById(Long id) {

        var exists = bookCategoriesRepository.existsById(id);
        var category = bookCategoriesRepository.findById(id).orElseThrow(
                () -> new BadRequestException(
                        "delete book - category", id, "This book - category doesn't exist in the library")
        );

        List<Book> books = bookRepository.findAllByBookCategories(category);

        if (!books.isEmpty()) {
            books.forEach(
                    book -> book.setBookCategories(null)
            );
        }

        if (exists)
            bookCategoriesRepository.deleteById(id);

        return mapper.map(category, BookCategoriesResponseDto.class);
    }
}