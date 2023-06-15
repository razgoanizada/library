package raz.projects.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import raz.projects.library.dto.request.CustomerRequestDto;
import raz.projects.library.dto.response.CustomerResponseDto;
import raz.projects.library.enums.CustomerType;
import raz.projects.library.repository.CustomerRepository;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    CustomerRepository customerRepository;

    private static final String getCustomers = "/customers";

    private static final String addCustomer = getCustomers + "/add";

    @BeforeEach
    public void cleanUp() {
        customerRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertThat(restTemplate).isNotNull();
    }

    @Test
    void getCustomers () {

        var response =  restTemplate.getForEntity(getCustomers, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getCustomersPage () {

        var response =  restTemplate.getForEntity(getCustomers + "/page", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void addCustomer_httpStatus () {
        ResponseEntity<CustomerResponseDto> res = getCustomerResponseDtoResponseEntity();

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void addCustomerToDB () {
        CustomerRequestDto customer = createCustomer();

        restTemplate.postForEntity(addCustomer, customer, CustomerResponseDto.class);
        assertThat(customerRepository.count()).isEqualTo(1L);
    }


    @Test
    void addCustomer_firstNameHasNull () {
        CustomerRequestDto customer = createCustomer();
        customer.setFirstName(null);

       addCustomer_BadRequest(customer);

    }

    @Test
    void addCustomer_firstNameHas1Characters () {
        CustomerRequestDto customer = createCustomer();
        customer.setFirstName("r");

        addCustomer_BadRequest(customer);
    }

    @Test
    void addCustomer_lastNameHasNull () {
        CustomerRequestDto customer = createCustomer();
        customer.setLastName(null);

        addCustomer_BadRequest(customer);

    }

    @Test
    void addCustomer_lastNameHas1Characters () {
        CustomerRequestDto customer = createCustomer();
        customer.setLastName("r");

        addCustomer_BadRequest(customer);


    }

    @Test
    void addCustomer_UniqueEmail () {
        CustomerRequestDto customer1 = createCustomer();
        CustomerRequestDto customer2 = createCustomer();

        customer2.setTz("015901390");

        //     check:
//    customer2.setEmail("razcohen@gmail.com");

        restTemplate.postForEntity(addCustomer, customer1, CustomerResponseDto.class);
        addCustomer_BadRequest(customer2);

    }

    @Test
    void addCustomer_EmailHasNull () {
        CustomerRequestDto customer = createCustomer();
        customer.setEmail(null);

        addCustomer_BadRequest(customer);

    }

    @Test
    void addCustomer_EmailRegexp () {

        CustomerRequestDto customer = createCustomer();
        customer.setEmail("123456");
        addCustomer_BadRequest(customer);
    }

    @Test
    void addCustomer_PhoneHasNull () {

        CustomerRequestDto customer = createCustomer();
        customer.setPhone(null);
        addCustomer_BadRequest(customer);
    }

    @Test
    void addCustomer_Phone8Characters () {

       CustomerRequestDto customer = createCustomer();
        customer.setPhone("12345678");
        addCustomer_BadRequest(customer);
    }


    @Test
    void addCustomer_tzHasNull () {

        CustomerRequestDto customer = createCustomer();
        customer.setTz(null);
        addCustomer_BadRequest(customer);
    }

    @Test
    void addCustomer_tzHasLetters () {

        CustomerRequestDto customer = createCustomer();
        customer.setTz("12345678r");
        addCustomer_BadRequest(customer);
    }

    @Test
    void addCustomer_UniqueTz () {

        CustomerRequestDto customer1 = createCustomer();
        CustomerRequestDto customer2 = createCustomer();
        customer2.setEmail("razgsdgd@fdsf.com");

//     check:
//        customer2.setTz("015901390");

        restTemplate.postForEntity(addCustomer, customer1, CustomerResponseDto.class);
        addCustomer_BadRequest(customer2);
    }

    @Test
    void addCustomer_customerTypeHadNull () {

        CustomerRequestDto customer = createCustomer();
        customer.setCustomerType(null);

        addCustomer_BadRequest(customer);
    }

    @Test
    void getCustomerById_OK() {
        ResponseEntity<CustomerResponseDto> res = getCustomerResponseDtoResponseEntity();
        var id = Objects.requireNonNull(res.getBody()).getId();

        var response = restTemplate.getForEntity(getCustomers + "/" + id, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getCustomerById_NotFound() {
        ResponseEntity<CustomerResponseDto> res = getCustomerResponseDtoResponseEntity();
        var id = Objects.requireNonNull(res.getBody()).getId() + 1;

        var response = restTemplate.getForEntity(getCustomers + "/" + id, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    //**********************************************
    // help method
    //************************************************************

    private ResponseEntity<CustomerResponseDto> getCustomerResponseDtoResponseEntity() {
        CustomerRequestDto customer = createCustomer();
        return restTemplate.postForEntity(addCustomer, customer, CustomerResponseDto.class);
    }

    private void addCustomer_BadRequest(CustomerRequestDto customer) {
        var res = restTemplate.postForEntity(addCustomer, customer, CustomerResponseDto.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private CustomerRequestDto createCustomer () {
        return CustomerRequestDto.builder()
                .firstName("raz")
                .lastName("goanizada")
                .email("razgoanizada@gmail.com")
                .phone("0524046007")
                .tz("318969433")
                .customerType(CustomerType.simple)
                .isActive(true)
                .build();
    }


}
