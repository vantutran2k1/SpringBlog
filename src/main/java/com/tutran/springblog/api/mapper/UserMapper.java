package com.tutran.springblog.api.mapper;

import com.tutran.springblog.api.entity.User;
import com.tutran.springblog.api.payload.authentication.RegisterDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User registerDtoToUser(RegisterDto registerDto);
}
