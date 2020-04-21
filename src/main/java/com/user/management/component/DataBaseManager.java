package com.user.management.component;

import com.user.management.dto.CarDto;
import com.user.management.dto.ContactDto;
import com.user.management.dto.UserDto;
import com.user.management.repository.CarRepository;
import com.user.management.repository.UserRespository;
import com.user.management.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author patel
 */
@Component
@Log4j2
@RequiredArgsConstructor
public class DataBaseManager {

    private final CarRepository carRepository;
    private final UserRespository userRespository;
    private final UserService userService;

    @Scheduled(cron = "0 30 5 * * MON")
    private void resetDataBase() {
        log.info("Cron Job Ran To Reset DataBase");
        carRepository.deleteAll();
        userRespository.deleteAll();
        insertDefaultUser();
    }

    private void insertDefaultUser() {
        log.info("Inserting Default User");

        ContactDto contact = new ContactDto();
        contact.setEmail("uday@cloud.com");
        contact.setPhone("0123456789");

        CarDto carOne = new CarDto();
        carOne.setModel("Porsche 718 GT");
        carOne.setYear(2019);

        CarDto carTwo = new CarDto();
        carTwo.setModel("Porsche 911 Turbo");
        carTwo.setYear(2020);

        UserDto user = new UserDto();
        user.setFirstName("Uday");
        user.setLastName("Patel");
        user.setAge(25);
        user.setContact(contact);
        user.setCars(Arrays.asList(carOne, carTwo));

        userService.saveUser(user);
    }
}
