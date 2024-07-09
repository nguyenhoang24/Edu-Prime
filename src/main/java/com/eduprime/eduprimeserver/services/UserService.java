package com.eduprime.eduprimeserver.services;


import com.eduprime.eduprimeserver.domains.User;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.UserDto;
import com.eduprime.eduprimeserver.dtos.request.PageReq;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface UserService {

    UserDto findByUserName(String username);

    User getByUserName(String username);

    UserDto createUser(UserDto userDto);

//    UserResponse getUserFromToken(String token);

    UserDto findUserById(String id);

    byte[] getImage(String userId) throws IOException;

    BaseResponse<Page> paging(PageReq pageRequest);
}
