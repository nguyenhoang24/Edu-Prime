package com.eduprime.eduprimeserver.services.impl;

import com.eduprime.eduprimeserver.common.HttpStatusCodes;
import com.eduprime.eduprimeserver.common.HttpStatusMessages;
import com.eduprime.eduprimeserver.common.RoleCommon;
import com.eduprime.eduprimeserver.domains.Role;
import com.eduprime.eduprimeserver.domains.User;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.UserDto;
import com.eduprime.eduprimeserver.dtos.request.PageReq;
import com.eduprime.eduprimeserver.exceptions.EntityNotFoundException;
import com.eduprime.eduprimeserver.repositories.UserRepository;
import com.eduprime.eduprimeserver.services.JwtService;
import com.eduprime.eduprimeserver.services.RoleService;
import com.eduprime.eduprimeserver.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final EntityManager manager;

    @Override
    public UserDto findByUserName(String username) {
        User user = this.userRepository.findByUserName(username).orElse(null);
        return UserDto.of(user);
    }

    @Override
    public User getByUserName(String username) {
        return this.userRepository.findByUserName(username).orElseThrow(() ->
                new EntityNotFoundException("Account with username: %s not found!".formatted(username)));
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        Assert.notNull(userDto.getUsername(), "username not null!");
        Assert.notNull(userDto.getPassword(), "password not null!");

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .address(userDto.getAddress())
                .profile(userDto.getProfile())
                .status(1)
                .build();

        Set<Role> roleList = new HashSet<>();
        userDto.getRoleList().add(RoleCommon.DEFAULT_ROLE_USERS);
        userDto.getRoleList().forEach(roleDto -> {
            Role role = roleService.findByRoleName(roleDto);
            roleList.add(role);
        });
        user.setRoleList(roleList);

        user = userRepository.save(user);

        return UserDto.of(user);
    }

//    @Override
//    public UserResponse getUserFromToken(String token) {
//        Assert.notNull(token, "Token not null!");
//
//        var username = this.jwtService.extractUsername(token);
//
//        User user = this.getByUserName(username);
//
//        UserResponse response = UserResponse.builder()
//                .id(user.getId())
//                .username(user.getUsername())
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .email(user.getEmail())
//                .address(user.getAddress())
//                .phoneNumber(user.getPhoneNumber())
//                .address(user.getAddress())
//                .status(user.getStatus())
//                .build();
//
//        return response;
//    }

    @Override
    public UserDto findUserById(String id) {
        var user = this.userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Account with id: %s not found!".formatted(id)));
        return UserDto.of(user);
    }

    @Override
    public byte[] getImage(String userId) throws IOException {
        Assert.notNull(userId, "userId not null!");

        UserDto user = this.findUserById(userId);
        String fileUrl = user.getProfile();
        if (fileUrl == null || fileUrl.isEmpty()) {
            return getDefaultImageBytes();
        }

        Path filename = Paths.get(user.getProfile());

        if (!Files.exists(filename)) {
            return getDefaultImageBytes();
        }

        return Files.readAllBytes(filename);
    }

    @Override
    public BaseResponse<Page> paging(PageReq pageRequest) {
        if (pageRequest.getPageIndex() == null || pageRequest.getPageSize() == null) {
            return BaseResponse.error(HttpStatusCodes.BAD_REQUEST, HttpStatusMessages.BAD_REQUEST
                    + " Invalid page request");
        }

        int pageIndex = pageRequest.getPageIndex();
        int pageSize = pageRequest.getPageSize();

        if (pageIndex > 0) {
            pageIndex--;
        } else {
            pageIndex = 0;
        }
        String whereClause = " where (1=1) ";
        String sqlCount = "select count(entity.id) from User as entity ";
        String sql = "select entity from User as entity ";

        if (pageRequest.getFromDate() != null) {
            whereClause += " AND ( entity.createDate >= :fromDate) ";
        }

        if (pageRequest.getToDate() != null) {
            whereClause += " AND ( entity.createDate <= :toDate) ";
        }

        if (pageRequest.getName() != null) {
            whereClause += " AND ( entity.username LIKE :username  OR entity.firstName LIKE :firstName OR entity.lastName LIKE :lastName) ";
        }

        sql += whereClause;
        sqlCount += whereClause;

        TypedQuery<User> q = manager.createQuery(sql, User.class);
        TypedQuery<Long> qCount = manager.createQuery(sqlCount, Long.class);

//		if (dto.getText() != null && StringUtils.hasText(dto.getText())) {
//			q.setParameter("text", '%' + dto.getText().trim() + '%');
//			qCount.setParameter("text", '%' + dto.getText().trim() + '%');
//		}

        if (pageRequest.getName() != null) {
            String namePattern = "%" + pageRequest.getName() + "%";

            q.setParameter("username", namePattern);
            qCount.setParameter("username", namePattern);

            q.setParameter("firstName", namePattern);
            qCount.setParameter("firstName", namePattern);

            q.setParameter("lastName", namePattern);
            qCount.setParameter("lastName", namePattern);
        }

        if (pageRequest.getFromDate() != null) {
            DateTime dateTime = new DateTime(pageRequest.getFromDate());
            LocalDateTime fromDate = dateTime.toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            q.setParameter("fromDate", fromDate);
            qCount.setParameter("fromDate", fromDate);
        }

        if (pageRequest.getToDate() != null) {
            DateTime dateTime = new DateTime(pageRequest.getToDate());
            LocalDateTime toDate = dateTime.toLocalDateTime();
            q.setParameter("toDate", toDate);
            qCount.setParameter("toDate", toDate);
        }

        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<User> entities = q.getResultList();
        var userDto = entities.stream().map(UserDto::of).collect(Collectors.toList());
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<UserDto> result = new PageImpl<>(userDto, pageable, count);

        return BaseResponse.of(result, HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    private byte[] getDefaultImageBytes() throws IOException {
        Path defaultImagePath = Paths.get("uploads/images/", "default-image.jpg");
        return Files.readAllBytes(defaultImagePath);
    }
}
