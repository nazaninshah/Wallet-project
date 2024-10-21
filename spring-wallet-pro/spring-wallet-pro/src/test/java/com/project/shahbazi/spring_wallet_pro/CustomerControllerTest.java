package com.project.shahbazi.spring_wallet_pro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shahbazi.spring_wallet_pro.entity.CustomerEntity;
import com.project.shahbazi.spring_wallet_pro.entity.Gender;
import com.project.shahbazi.spring_wallet_pro.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    public void testCreateCustomer() throws Exception {
        // Test data
        CustomerEntity customer = new CustomerEntity();
        customer.setNationalId("3860877161");
        customer.setName("naz");
        customer.setSurname("shahbazi");
        customer.setPhoneNumber("09210742098");
        customer.setEmail("nazi.shbz@gmail.com");

        // Convert LocalDate to Date
        LocalDate localDate = LocalDate.of(1995, 6, 19);
        Date birthdate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        customer.setBirthdate(birthdate); // Set the birthdate here

        customer.setGender(Gender.FEMALE);
        customer.setMilitaryServiceStatus("None");

        // Mock behavior of customerService
        Mockito.doReturn(customer).when(customerService).createCustomer(any(CustomerEntity.class));

        // Convert customer to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String customerJson = objectMapper.writeValueAsString(customer);

        // Send POST request to the correct URL
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isCreated()) // Expecting HTTP 201 status
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("naz"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("shahbazi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("09210742098"));
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        // Test data
        CustomerEntity customer1 = new CustomerEntity();
        customer1.setName("naz");
        customer1.setSurname("shahbazi");

        CustomerEntity customer2 = new CustomerEntity();
        customer2.setName("ali");
        customer2.setSurname("ahmadi");

        List<CustomerEntity> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);

        // Mock behavior of customerService
        Mockito.when(customerService.getAllCustomers()).thenReturn(customers);

        // Send GET request
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk()) // Expecting HTTP 200 status
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("naz"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("ali"));
    }

    @Test
    public void testGetCustomerById() throws Exception {
        // Test data
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setName("naz");
        customer.setSurname("shahbazi");

        // Mock behavior of customerService
        Mockito.when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customer));

        // Send GET request
        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk()) // Expecting HTTP 200 status
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("naz"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("shahbazi"));
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        // Test data for existing customer
        CustomerEntity existingCustomer = new CustomerEntity();
        existingCustomer.setId(1L);
        existingCustomer.setNationalId("3860877161");
        existingCustomer.setName("naz");
        existingCustomer.setSurname("shahbazi");
        existingCustomer.setPhoneNumber("09210742098");
        existingCustomer.setEmail("nazi.shbz@gmail.com");

        LocalDate localDate = LocalDate.of(1995, 6, 19);
        Date birthdate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        existingCustomer.setBirthdate(birthdate);
        existingCustomer.setGender(Gender.FEMALE);
        existingCustomer.setMilitaryServiceStatus("None");

        // Test data for updated customer
        CustomerEntity updatedCustomer = new CustomerEntity();
        updatedCustomer.setNationalId("3860877161");
        updatedCustomer.setName("naz_updated");
        updatedCustomer.setSurname("shahbazi_updated");
        updatedCustomer.setPhoneNumber("09210742099");
        updatedCustomer.setEmail("nazi_updated.shbz@gmail.com");
        updatedCustomer.setGender(Gender.FEMALE);
        updatedCustomer.setBirthdate(birthdate);
        updatedCustomer.setMilitaryServiceStatus("None");

        // Mock behavior of customerService
        Mockito.when(customerService.getCustomerById(1L)).thenReturn(Optional.of(existingCustomer));
        Mockito.when(customerService.updateCustomer(Mockito.anyLong(), Mockito.any(CustomerEntity.class)))
                .thenReturn(updatedCustomer);

        // Convert updated customer to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String customerJson = objectMapper.writeValueAsString(updatedCustomer);

        // Send PUT request
        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isOk()) // Expecting HTTP 200 status
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("naz_updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("shahbazi_updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("09210742099"));
    }


    @Test
    public void testDeleteCustomer() throws Exception {
        // Mock behavior of customerService
        Mockito.doNothing().when(customerService).deleteCustomer(1L);

        // Send DELETE request
        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isOk()) // Expecting HTTP 200 status
                .andExpect(MockMvcResultMatchers.content().string("Customer successfully deleted."));
    }

}
