package com.user.management.constants;

/**
 * @author patel
 */
public class ExceptionConstants {

    private ExceptionConstants(){

    }

    public static final String NEW_USER_SHOULD_NOT_HAVE_AN_ID_MSG = "New User Should Not Have An Id";
    public static final String USER_ID_NULL_MSG = "User Id Is Not Available";
    public static final String EMAIL_ALREADY_EXISTS_MSG = "Email Already Exists";
    public static final String PHONE_MUST_HAVE_NUMBERS_MSG = "Phone Must Have Numbers Only";
    public static final String PHONE_ALREADY_EXISTS_MSG = "Phone Already Exists";
    public static final String PHONE_LENGTH_INVALID_MSG = "Phone Number Minimum Length Must Be 8 Or Max Length Must Be 12";
    public static final String USER_NOT_FOUND_MSG = "User Is Not Available For The Id: ";

    public static final String CAR_NOT_FOUND_MSG = "Car Is Not Available For The Id: ";
}
