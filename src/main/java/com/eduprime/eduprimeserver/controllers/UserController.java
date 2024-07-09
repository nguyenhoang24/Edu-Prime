package com.eduprime.eduprimeserver.controllers;

import com.eduprime.eduprimeserver.dtos.request.PageReq;
import com.eduprime.eduprimeserver.services.UserService;
import com.eduprime.eduprimeserver.utils.ImageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Controller")
public class UserController {

    private final UserService userService;

//    @GetMapping("/getUserFromToken")
//    public ResponseEntity<?> getUserFromToken(@RequestParam String token) {
//        return ResponseEntity.ok(this.userService.getUserFromToken(token));
//    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        return ResponseEntity.ok(this.userService.findUserById(userId));
    }


    @GetMapping("/get-images/{userId}")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable("userId") String userId) {
        try {
            byte[] buffer = this.userService.getImage(userId);
            ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);

            String imageFormat = ImageUtils.getImageFormat(buffer);

            return ResponseEntity.ok()
                    .contentLength(buffer.length)
                    .contentType(MediaType.parseMediaType("image/" + imageFormat))
                    .body(byteArrayResource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Paging user", description = "Phan trang user")
    @PostMapping("/paging")
    public ResponseEntity<?> pagingUser(@RequestBody PageReq pageReq) {
        return ResponseEntity.ok(this.userService.paging(pageReq));
    }


}
