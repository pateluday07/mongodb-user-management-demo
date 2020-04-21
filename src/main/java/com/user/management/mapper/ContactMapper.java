package com.user.management.mapper;

import com.user.management.dto.ContactDto;
import com.user.management.model.Contact;
import org.mapstruct.Mapper;

/**
 * @author patel
 */
@Mapper
public interface ContactMapper {

    Contact toModel(ContactDto contactDto);

    ContactDto toDto(Contact contact);
}
