package com.user.management.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author patel
 */
@Getter
@Setter
@Document
public class Car {

    @Id
    private String id;
    private String model;
    private Integer year;
}
