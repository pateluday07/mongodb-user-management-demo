package com.user.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.management.model.Car;
import com.user.management.model.Contact;
import com.user.management.model.User;
import com.user.management.repository.UserRespository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static com.user.management.constants.ExceptionConstants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    private static final String USERS_API_PREFIX = "/api/users";

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRespository userRespository;

    private User user;
    private Contact contact;
    private List<Car> cars;

    @BeforeEach
    void initCars() {
        Car car = new Car();
        car.setModel("Porsche 911");
        car.setYear(2021);

        Car car1 = new Car();
        car1.setModel("BMW M6");
        car1.setYear(2020);

        this.cars = Arrays.asList(car, car1);
    }

    @BeforeEach
    void initContact() {
        Contact contact = new Contact();
        contact.setEmail("pateluday07@gmail.com");
        contact.setPhone("1234567890");
        this.contact = contact;
    }

    @BeforeEach
    void initUser() {
        User user = new User();
        user.setFirstName("Uday");
        user.setLastName("Patel");
        user.setAge(26);
        user.setContact(contact);
        user.setCars(cars);
        this.user = user;
    }

    @AfterEach
    void removeAllUsers() {
        userRespository.deleteAll();
    }

    @Test
    void saveUser_NoException_WhenDataIsValid() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post(USERS_API_PREFIX)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Uday"))
                .andExpect(jsonPath("$.lastName").value("Patel"))
                .andExpect(jsonPath("$.age").value(26));
    }

    @Test
    void saveUser_ThrowsException_IfUserIdAvailable() throws Exception {
        user.setId("test");
        mvc.perform(MockMvcRequestBuilders
                .post(USERS_API_PREFIX)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(NEW_USER_SHOULD_NOT_HAVE_AN_ID_MSG));
    }

    @Test
    void saveUser_ThrowsException_IfEmailExists() throws Exception {
        saveUser_NoException_WhenDataIsValid();
        mvc.perform(MockMvcRequestBuilders
                .post(USERS_API_PREFIX)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(status().reason(EMAIL_ALREADY_EXISTS_MSG));
    }

    @Test
    void saveUser_ThrowsException_IfPhoneExists() throws Exception {
        saveUser_NoException_WhenDataIsValid();

        contact.setEmail("abc@gmail.com");
        mvc.perform(MockMvcRequestBuilders
                .post(USERS_API_PREFIX)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(status().reason(PHONE_ALREADY_EXISTS_MSG));
    }

    @Test
    void saveUser_ThrowsException_IfPhoneIsNotNumber() throws Exception {
        contact.setPhone("xxxxx11");
        mvc.perform(MockMvcRequestBuilders
                .post(USERS_API_PREFIX)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(PHONE_MUST_HAVE_NUMBERS_MSG));
    }

    @Test
    void saveUser_ThrowsException_IfPhoneLengthIsNotValid() throws Exception {
        contact.setPhone("11111");
        mvc.perform(MockMvcRequestBuilders
                .post(USERS_API_PREFIX)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(PHONE_LENGTH_INVALID_MSG));
    }

    @Test
    void updateUser_ThrowsException_IfUserIdIsNull() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .put(USERS_API_PREFIX)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(USER_ID_NULL_MSG));
    }

    @Test
    void updateUser_ThrowsException_IfUserNotFound() throws Exception {
        String id = "111";
        user.setId(id);
        mvc.perform(MockMvcRequestBuilders
                .put(USERS_API_PREFIX)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(USER_NOT_FOUND_MSG.concat(id)));
    }

    @Test
    void updateUser_ThrowsException_IfEmailAlreadyExists() throws Exception {
        saveUser_NoException_WhenDataIsValid();

        User userToUpdate = saveInitialUser();
        userToUpdate.getContact().setEmail("pateluday07@gmail.com");
        mvc.perform(MockMvcRequestBuilders
                .put(USERS_API_PREFIX)
                .content(mapper.writeValueAsString(userToUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(status().reason(EMAIL_ALREADY_EXISTS_MSG));
    }

    @Test
    void updateUser_ThrowsException_IfPhoneAlreadyExists() throws Exception {
        saveUser_NoException_WhenDataIsValid();

        User userToUpdate = saveInitialUser();
        userToUpdate.getContact().setPhone("1234567890");
        mvc.perform(MockMvcRequestBuilders
                .put(USERS_API_PREFIX)
                .content(mapper.writeValueAsString(userToUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(status().reason(PHONE_ALREADY_EXISTS_MSG));
    }

    @Test
    void updateUser_NoException_IfDataIsValid() throws Exception {
        String firstName = "John";
        String lastName = "Doe";
        int age = 30;

        User userToUpdate = saveInitialUser();
        userToUpdate.setFirstName(firstName);
        userToUpdate.setLastName(lastName);
        userToUpdate.setAge(age);
        mvc.perform(MockMvcRequestBuilders
                .put(USERS_API_PREFIX)
                .content(mapper.writeValueAsString(userToUpdate))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    void getUserById_ThrowsException_IfUserNotFound() throws Exception {
        String id = "11";
        mvc.perform(MockMvcRequestBuilders
                .get(USERS_API_PREFIX.concat("/{userId}"), id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(USER_NOT_FOUND_MSG.concat(id)));
    }

    @Test
    void getUserById_NoException_IfUserFound() throws Exception {
        User initialUser = saveInitialUser();
        mvc.perform(MockMvcRequestBuilders
                .get(USERS_API_PREFIX.concat("/{userId}"), initialUser.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(initialUser.getId()));
    }

    @Test
    void getAllUsers_EmptyList_IfUsersNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(USERS_API_PREFIX)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAllUsers_ListOfUsers_IfUsersFound() throws Exception {
        saveInitialUser();

        mvc.perform(MockMvcRequestBuilders
                .get(USERS_API_PREFIX)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void deleteUser_NoException_IfUserDeleted() throws Exception {
        User initialUser = saveInitialUser();

        mvc.perform(MockMvcRequestBuilders
                .delete(USERS_API_PREFIX.concat("/{userId}"), initialUser.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_ThrowsException_IfUserNotFound() throws Exception {
        String id = "11";
        mvc.perform(MockMvcRequestBuilders
                .delete(USERS_API_PREFIX.concat("/{userId}"), id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(USER_NOT_FOUND_MSG.concat(id)));
    }

    @Test
    void getUserByEmail_NoException_IfUserFound() throws Exception {
        User initialUser = saveInitialUser();

        mvc.perform(MockMvcRequestBuilders
                .get(USERS_API_PREFIX.concat("/by-email/{email}"), initialUser.getContact().getEmail())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserByEmail_ThrowsException_IfUserNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(USERS_API_PREFIX.concat("/by-email/{email}"), user.getContact().getEmail())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(USER_NOT_FOUND_BY_EMAIL_MSG.concat(user.getContact().getEmail())));
    }

    @Test
    void getUserByPhone_NoException_IfUserFound() throws Exception {
        User initialUser = saveInitialUser();

        mvc.perform(MockMvcRequestBuilders
                .get(USERS_API_PREFIX.concat("/by-phone/{phone}"), initialUser.getContact().getPhone())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserByPhone_ThrowsException_IfUserNotFound() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(USERS_API_PREFIX.concat("/by-phone/{phone}"), user.getContact().getPhone())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(USER_NOT_FOUND_BY_PHONE_MSG.concat(user.getContact().getPhone())));
    }

    private User saveInitialUser() throws Exception {
        contact.setEmail("abc@gmail.email");
        contact.setPhone("00123456789");
        String content = mvc.perform(MockMvcRequestBuilders
                .post(USERS_API_PREFIX)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readValue(content, User.class);
    }

}
