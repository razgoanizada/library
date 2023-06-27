package raz.projects.library.service;

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
import raz.projects.library.entity.Librarian;
import raz.projects.library.entity.Permissions;
import raz.projects.library.errors.BadRequestException;
import raz.projects.library.errors.ResourceNotFoundException;
import raz.projects.library.repository.LibrarianRepository;
import raz.projects.library.repository.PermissionsRepository;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class LibrarianServiceImpl implements LibrarianService, UserDetailsService {

    private final LibrarianRepository librarianRepository;
    private final PermissionsRepository permissionsRepository;
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
            String permission, String firstName, String lastName, String phone, String tz, String useName) {

        Specification<Librarian> specification = Specification.where(null);

        if (permission != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("permission"), permission));
        }

        if (firstName != null && !firstName.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%" ));
        }

        if (lastName != null && !lastName.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%" ));
        }

        if (phone != null && !phone.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), "%" + phone.toLowerCase() + "%" ));
        }

        if (tz != null && !tz.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("tz")), "%" + tz.toLowerCase() + "%" ));
        }

        if (useName != null && !useName.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("useName")), "%" + useName.toLowerCase() + "%" ));
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.fromString(sortDir), sortBy);

        Page<Librarian> page = librarianRepository.findAll(specification ,pageable);

        return LibrarianPageDto.builder()
                .results(page.stream().map(librarian -> mapper.map(librarian, LibrarianResponseDto.class)).toList())
                .totalPages(page.getTotalPages())
                .totalBooks(page.getTotalElements())
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

        librarianRepository.save(librarian);

        var response = mapper.map(librarian, LibrarianResponseDto.class);
        response.setPermission(librarian.getPermission().getPermission());

        return response;

    }


    @Override
    public LibrarianResponseDto getLibrarianById(Long id) {

        var librarian = librarianRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException( "get librarian" ,id, "This librarian doesn't exist in the library")
        );
        return mapper.map(librarian, LibrarianResponseDto.class);
    }

    @Override
    public LibrarianResponseDto updateLibrarianById(LibrarianUpdate dto, Long id) {


        var librarian = librarianRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException( "get librarian" ,id, "This librarian doesn't exist in the library")
        );

        librarian.setFirstName(dto.getFirstName());
        librarian.setLastName(dto.getLastName());
        librarian.setPhone(dto.getPhone());
        librarian.setGender(dto.getGender());
        librarian.setAddress(dto.getAddress());
        librarian.setDateOfBirth(dto.getDateOfBirth());
        librarian.getPermission().setPermission(dto.getPermission());

        var saved = librarianRepository.save(librarian);

       var response = mapper.map(saved, LibrarianResponseDto.class);
       response.setPermission(librarian.getPermission().getPermission());

       return response;
    }

    public LibrarianResponseDto librarianChangePassword(LibrarianChangePassword dto, Long id){

        var librarian = librarianRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException( "get librarian" ,id, "This librarian doesn't exist in the library")
        );

        librarian.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        var saved = librarianRepository.save(librarian);

        return mapper.map(saved, LibrarianResponseDto.class);
    }

    @Override
    public LibrarianResponseDto deleteLibrarianById(Long id) {

        var exists = librarianRepository.existsById(id);
        var librarian = librarianRepository.findById(id).orElseThrow(
                () -> new BadRequestException("delete librarian", id, "This librarian does not exist in the library")
        );

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
