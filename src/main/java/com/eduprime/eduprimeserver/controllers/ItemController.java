package com.eduprime.eduprimeserver.controllers;

import com.eduprime.eduprimeserver.dtos.request.ItemRequest;
import com.eduprime.eduprimeserver.dtos.request.PageReq;
import com.eduprime.eduprimeserver.services.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Tag(name = "Item Controller")
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "Get list item by lesson", description = "Lay ra danh sach item dua vao lessonId")
    @GetMapping("/lessons/{lessonId}")
    public ResponseEntity<?> getListLecturesDetails(@PathVariable String lessonId) {
        return ResponseEntity.ok(this.itemService.getListItemByLessonId(lessonId));
    }

    @Operation(summary = "get item by id", description = "Lay ra item dua vao id")
    @GetMapping("/{itemId}")
    public ResponseEntity<?> getItemById(@PathVariable String itemId) {
        return ResponseEntity.ok(this.itemService.getItemById(itemId));
    }

    @Operation(summary = "Create item", description = "Them moi item")
    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody ItemRequest request) {
        return ResponseEntity.ok(this.itemService.createItem(request));
    }

    @Operation(summary = "Update Item", description = "Sua item")
    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateItem(@RequestBody ItemRequest request, @PathVariable String itemId) {
        return ResponseEntity.ok(this.itemService.updateItem(request, itemId));
    }

    @GetMapping("/downloads/{documentId}")
    public ResponseEntity<ByteArrayResource> downloadDocument(@PathVariable String documentId) throws IOException {
        return this.itemService.downloadDocument(documentId);
    }

    @GetMapping("/view-pdfs/{itemId}")
    public ResponseEntity<byte[]> viewPdf(@PathVariable String itemId) {
        try {
            byte[] pdfContent = this.itemService.viewPdf(itemId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Paging item", description = "Phan trang tai lieu")
    @PostMapping("/paging")
    public ResponseEntity<?> pagingItem(@RequestBody PageReq pageReq) {
        return ResponseEntity.ok(this.itemService.paging(pageReq));
    }
}
