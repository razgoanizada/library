package raz.projects.library;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import raz.projects.library.dto.request.LibrarianRequestDto;
import raz.projects.library.dto.response.LibrarianResponseDto;
import raz.projects.library.repository.LibrarianRepository;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LibrarianTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    LibrarianRepository librarianRepository;

    private static final String getLibrarians = "/online-library/librarians";
    private static final String addLibrarian = getLibrarians + "/add";

    @BeforeEach
    public void cleanUp() {
        librarianRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertThat(restTemplate).isNotNull();
    }

    @Test
    void getLibrarians () {
        
        var response =  restTemplate.getForEntity(getLibrarians, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getLibrariansPage () {

        var response =  restTemplate.getForEntity(getLibrarians + "/page", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void addLibrarian_httpStatus() {
        LibrarianRequestDto librarian = createLibrarian();
        ResponseEntity<LibrarianResponseDto> response = restTemplate.postForEntity(addLibrarian, librarian, LibrarianResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void addLibrarian_db () {
        LibrarianRequestDto librarian = createLibrarian();

        restTemplate.postForEntity(addLibrarian, librarian, LibrarianResponseDto.class);
        assertThat(librarianRepository.count()).isEqualTo(1L);
    }

    @Test
    void addLibrarian_fullNameHasNull () {
        LibrarianRequestDto librarian = createLibrarian();
        librarian.setFullName(null);
        addLibrarian_BadRequest(librarian);
    }

    @Test
    void addLibrarian_fullNameHas2Characters () {
        LibrarianRequestDto librarian = createLibrarian();
        librarian.setFullName("ra");
        addLibrarian_BadRequest(librarian);

    }

    @Test
    void addLibrarian_UniqueUserName () {
        LibrarianRequestDto librarian1 = createLibrarian();
        LibrarianRequestDto librarian2 = createLibrarian();
        librarian2.setEmail("sffsf@dsfsdfsdfsd.com");
        librarian2.setTz("015901390");

//     check:
//    librarian2.setUserName("sfsdfsdfsdf");

        restTemplate.postForEntity(addLibrarian, librarian1, LibrarianResponseDto.class);
        addLibrarian_BadRequest(librarian2);

    }

    @Test
    void addLibrarian_UserNameHasNull () {
        LibrarianRequestDto librarian = createLibrarian();
        librarian.setUserName(null);
        addLibrarian_BadRequest(librarian);

    }

    @Test
    void addLibrarian_UserNameHas2Characters () {
        LibrarianRequestDto librarian = createLibrarian();
        librarian.setUserName("ra");
        addLibrarian_BadRequest(librarian);

    }

    @Test
    void addLibrarian_PasswordHasNull () {
        LibrarianRequestDto librarian = createLibrarian();
        librarian.setPassword(null);
        addLibrarian_BadRequest(librarian);

    }

    @Test
    void addLibrarian_PasswordHasNotStrong () {
        LibrarianRequestDto librarian = createLibrarian();
        librarian.setPassword("123456");
        addLibrarian_BadRequest(librarian);

    }

    @Test
    void addLibrarian_PasswordIsHashedInDatabase () {
        LibrarianRequestDto librarian = createLibrarian();
        restTemplate.postForEntity(addLibrarian, librarian, LibrarianResponseDto.class);
        assertThat(librarian.getPassword()).isNotEqualTo(librarianRepository.findAll().get(0).getPassword());

    }

    @Test
    void addLibrarian_UniqueEmail () {

        LibrarianRequestDto librarian1 = createLibrarian();
        LibrarianRequestDto librarian2 = createLibrarian();
        librarian2.setUserName("Goa587");
        librarian2.setTz("015901390");

//     check:
//    librarian2.setEmail("razgsdgd@fdsf.com");

        restTemplate.postForEntity(addLibrarian, librarian1, LibrarianResponseDto.class);
        addLibrarian_BadRequest(librarian2);
    }

    @Test
    void addLibrarian_EmailHasNull () {

        LibrarianRequestDto librarian = createLibrarian();
        librarian.setEmail(null);
        addLibrarian_BadRequest(librarian);
    }


    @Test
    void addLibrarian_EmailRegexp () {

        LibrarianRequestDto librarian = createLibrarian();
        librarian.setEmail("123456");
        addLibrarian_BadRequest(librarian);
    }

    @Test
    void addLibrarian_PhoneHasNull () {

        LibrarianRequestDto librarian = createLibrarian();
        librarian.setPhone(null);
        addLibrarian_BadRequest(librarian);
    }

    @Test
    void addLibrarian_Phone8Characters () {

        LibrarianRequestDto librarian = createLibrarian();
        librarian.setPhone("12345678");
        addLibrarian_BadRequest(librarian);
    }

    @Test
    void addLibrarian_tzHasNull () {

        LibrarianRequestDto librarian = createLibrarian();
        librarian.setTz(null);
        addLibrarian_BadRequest(librarian);
    }

    @Test
    void addLibrarian_tzHasLetters () {

        LibrarianRequestDto librarian = createLibrarian();
        librarian.setTz("12345678r");
        addLibrarian_BadRequest(librarian);
    }

    @Test
    void addLibrarian_UniqueTz () {

        LibrarianRequestDto librarian1 = createLibrarian();
        LibrarianRequestDto librarian2 = createLibrarian();
        librarian2.setUserName("Goa587");
        librarian2.setEmail("razgsdgd@fdsf.com");

//     check:
//    librarian2.setTz("015901390");

        restTemplate.postForEntity(addLibrarian, librarian1, LibrarianResponseDto.class);
        addLibrarian_BadRequest(librarian2);
    }

    @Test
    void addLibrarian_UniqueTzIsrael () {

        LibrarianRequestDto librarian = createLibrarian();
        librarian.setTz("318969432");
        addLibrarian_BadRequest(librarian);
    }

    @Test
    void getLibrarianById_OK() {
        ResponseEntity<LibrarianResponseDto> res = getLibrarianResponseDtoResponseEntity();
        var id = Objects.requireNonNull(res.getBody()).getId();

        var response = restTemplate.getForEntity(getLibrarians + "/" + id, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    void getLibrarianById_NotFound() {
        ResponseEntity<LibrarianResponseDto> res = getLibrarianResponseDtoResponseEntity();
        var id = Objects.requireNonNull(res.getBody()).getId() + 1;

        var response = restTemplate.getForEntity(getLibrarians + "/" + id, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    void deleteBookById_Accepted() {
        ResponseEntity<LibrarianResponseDto> res = getLibrarianResponseDtoResponseEntity();
        var id = Objects.requireNonNull(res.getBody()).getId();

        var response = restTemplate.exchange(getLibrarians + "/" + id, HttpMethod.DELETE, HttpEntity.EMPTY, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    void deleteBookById_Bad_Request() {
        ResponseEntity<LibrarianResponseDto> res = getLibrarianResponseDtoResponseEntity();
        var id = Objects.requireNonNull(res.getBody()).getId() + 1;

        var response = restTemplate.exchange(getLibrarians + "/" + id, HttpMethod.DELETE, HttpEntity.EMPTY, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    //**************************************************
    //help method
    //*******************************************************

    private void addLibrarian_BadRequest(LibrarianRequestDto librarian) {
        var res = restTemplate.postForEntity(addLibrarian, librarian, LibrarianResponseDto.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<LibrarianResponseDto> getLibrarianResponseDtoResponseEntity() {
        LibrarianRequestDto librarian = createLibrarian();
        return restTemplate.postForEntity(addLibrarian, librarian, LibrarianResponseDto.class);
    }

    private LibrarianRequestDto createLibrarian () {

        return LibrarianRequestDto.builder()
                .fullName("raz goanizada")
                .userName("raz123456789")
                .password("666Raz2!33")
                .email("razfdfdf@gmail.com")
                .phone("dfsfsdfsdf")
                .tz("318969433")
                .build();
    }

}


