package com.user.management.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author patel
 */
@Getter
@Setter
@NoArgsConstructor
@Document
public class User {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private Integer age;
    private Contact contact;
    @DBRef(lazy = true)
    private List<Car> cars;
}
