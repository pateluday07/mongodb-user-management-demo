package com.user.management.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * @author patel
 */
@Getter
@Setter
public class Contact {

    @Indexed(unique = true)
    private String email;
    @Indexed(unique = true)
    private String phone;
}
