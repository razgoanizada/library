package raz.projects.library.service.Librarian;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import raz.projects.library.dto.pages.LibrarianPageDto;
import raz.projects.library.dto.request.LibrarianRequestDto;
import raz.projects.library.dto.response.LibrarianResponseDto;
import raz.projects.library.dto.update.LibrarianChangePassword;
import raz.projects.library.dto.update.LibrarianUpdate;
import raz.projects.library.entity.*;
import raz.projects.library.errors.BadRequestException;
import raz.projects.library.errors.ResourceNotFoundException;
import raz.projects.library.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class LibrarianServiceImpl implements LibrarianService, UserDetailsService {

    private final LibrarianRepository librarianRepository;
    private final PermissionsRepository permissionsRepository;
    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;
    private final BorrowRepository borrowRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<LibrarianResponseDto> getLibrarians () {

        return librarianRepository.findAll()
                .stream()
                .map(librarian -> mapper.map(librarian, LibrarianResponseDto.class))
                .toList();
    }

    @Override
    public LibrarianPageDto getLibrariansPage(
            int pageNo, int pageSize, String sortBy, String sortDir,
            String permission, String firstName, String lastName, String phone, String tz, String userName) {

        Specification<Librarian> specification = Specification.where(null);

        if (permission != null && !permission.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("permission"), permission));
        }

        if (firstName != null && !firstName.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(
                            root.get("firstName")), "%" + firstName.toLowerCase() + "%" ));
        }

        if (lastName != null && !lastName.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(
                            root.get("lastName")), "%" + lastName.toLowerCase() + "%" ));
        }

        if (phone != null && !phone.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(
                            root.get("phone")), "%" + phone.toLowerCase() + "%" ));
        }

        if (tz != null && !tz.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(
                            root.get("tz")), "%" + tz.toLowerCase() + "%" ));
        }

        if (userName != null && !userName.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(
                            root.get("userName")), "%" + userName.toLowerCase() + "%" ));
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.fromString(sortDir), sortBy);

        Page<Librarian> page = librarianRepository.findAll(specification ,pageable);

        return LibrarianPageDto.builder()
                .results(page.stream().map(librarian -> mapper.map(librarian, LibrarianResponseDto.class)).toList())
                .totalPages(page.getTotalPages())
                .totalLibrarians(page.getTotalElements())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .build();
    }


    @Override
    public LibrarianResponseDto addLibrarian (LibrarianRequestDto dto) {

        Permissions permission =  permissionsRepository.findPermissionsByPermissionIgnoreCase(dto.getPermission());

        if (permission == null) {
            throw new BadRequestException(
                    "add librarian - permissions",
                    dto.getPermission(),
                    "This permissions doesn't exist in the library");
        }

        var librarian =  mapper.map(dto, Librarian.class);
        librarian.setPassword(passwordEncoder.encode(dto.getPassword()));
        librarian.setPermission(permission);
        librarian.setImage(permission.getImage());

        librarianRepository.save(librarian);

        var response = mapper.map(librarian, LibrarianResponseDto.class);
        response.setPermission(librarian.getPermission().getPermission());

        return response;

    }


    @Override
    public LibrarianResponseDto getLibrarianById(Long id) {

        var librarian = librarianRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "get librarian" ,id, "This librarian doesn't exist in the library")
        );
        return mapper.map(librarian, LibrarianResponseDto.class);
    }

    @Override
    public Librarian getLibrarianByUserName(String userName) {

        return librarianRepository.findLibrarianByUserNameIgnoreCase(userName).orElseThrow(
                () -> new ResourceNotFoundException(
                        "get librarian" ,userName, "This librarian doesn't exist in the library")
        );
    }

    @Override
    public void updateLibrarianLastLogin(String userName) {

        var librarian = librarianRepository.findLibrarianByUserNameIgnoreCase(userName).orElseThrow(
                () -> new ResourceNotFoundException(
                        "get librarian" ,userName, "This librarian doesn't exist in the library")
        );

        librarian.setLastLogin(LocalDateTime.now());
    }

    @Override
    public LibrarianResponseDto updateLibrarianById(LibrarianUpdate dto, Long id) {

        Permissions permission =  permissionsRepository.findPermissionsByPermissionIgnoreCase(dto.getPermission());

        if (permission == null) {
            throw new BadRequestException(
                    "add librarian - permissions",
                    dto.getPermission(),
                    "This permissions doesn't exist in the library");
        }


        var librarian = librarianRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "get librarian" ,id, "This librarian doesn't exist in the library")
        );

        if (librarian.getUserName().equals("admin")) {
            throw new BadRequestException(
                    "update librarian", "Unable to update admin user");
        }

        librarian.setFirstName(dto.getFirstName());
        librarian.setLastName(dto.getLastName());
        librarian.setPhone(dto.getPhone());
        librarian.setGender(dto.getGender());
        librarian.setAddress(dto.getAddress());
        librarian.setDateOfBirth(dto.getDateOfBirth());
        librarian.setPermission(permission);
        librarian.setImage(permission.getImage());


        var saved = librarianRepository.save(librarian);

       var response = mapper.map(saved, LibrarianResponseDto.class);
       response.setPermission(librarian.getPermission().getPermission());

       return response;
    }

    public LibrarianResponseDto librarianChangePassword(LibrarianChangePassword dto, Long id){

        var librarian = librarianRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "get librarian" ,id, "This librarian doesn't exist in the library")
        );

        if (librarian.getUserName().equals("admin")) {
            throw new BadRequestException(
                    "update librarian", "Unable to update admin user");
        }

        return change(dto, librarian);
    }

    public LibrarianResponseDto change(LibrarianChangePassword dto, Librarian librarian) {
        librarian.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        var saved = librarianRepository.save(librarian);

        return mapper.map(saved, LibrarianResponseDto.class);
    }

    @Override
    public LibrarianResponseDto deleteLibrarianById(Long id) {

        var exists = librarianRepository.existsById(id);
        var librarian = librarianRepository.findById(id).orElseThrow(
                () -> new BadRequestException(
                        "delete librarian", id, "This librarian does not exist in the library")
        );

        if (librarian.getUserName().equals("admin")) {
            throw new BadRequestException(
                    "delete librarian", "Unable to delete admin user");
        }

        List<Book> books = bookRepository.findAllByAddedBy(librarian);
        if (!books.isEmpty()) {
            books.forEach(
                    book -> book.setAddedBy(null)
            );
        }

        List<Customer> customers = customerRepository.findAllByAddedBy(librarian);
        if (!customers.isEmpty()) {
            customers.forEach(
                    customer -> customer.setAddedBy(null)
            );
        }

        List<Borrow> borrows = borrowRepository.findAllByAddedBy(librarian);
        if (!borrows.isEmpty()) {
            borrows.forEach(
                    borrow -> borrow.setAddedBy(null)
            );
        }


        if (exists)
            librarianRepository.deleteById(id);

        return mapper.map(librarian, LibrarianResponseDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var librarian = librarianRepository.findLibrarianByUserNameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        List<GrantedAuthority> permissions = new ArrayList<>();
        permissions.add(new SimpleGrantedAuthority(librarian.getPermission().getPermission()));


        return new User(librarian.getUserName(), librarian.getPassword(), permissions);
    }


}
