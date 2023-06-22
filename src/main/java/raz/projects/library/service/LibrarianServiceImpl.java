package raz.projects.library.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public LibrarianPageDto getLibrariansPage(int pageNo, int pageSize, String sortBy, String sortDir) {

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.fromString(sortDir), sortBy);

        Page<Librarian> page = librarianRepository.findAll(pageable);

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
        librarian.setPermission(permission);
        librarian.setPassword(passwordEncoder.encode(dto.getPassword()));

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


        librarian.setFullName(dto.getFullName());
        librarian.setPhone(dto.getPhone());
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
