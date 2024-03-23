package com.tutran.springblog.mapper;

import com.tutran.springblog.entity.User;
import com.tutran.springblog.payload.authentication.RegisterDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User registerDtoToUser(RegisterDto registerDto);
}
