package com.eduprime.eduprimeserver.services.impl;

import com.eduprime.eduprimeserver.common.HttpStatusCodes;
import com.eduprime.eduprimeserver.common.HttpStatusMessages;
import com.eduprime.eduprimeserver.datatype.FileType;
import com.eduprime.eduprimeserver.datatype.ItemType;
import com.eduprime.eduprimeserver.domains.Item;
import com.eduprime.eduprimeserver.domains.Lesson;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.ItemDto;
import com.eduprime.eduprimeserver.dtos.LessonDto;
import com.eduprime.eduprimeserver.dtos.request.ItemRequest;
import com.eduprime.eduprimeserver.dtos.request.PageReq;
import com.eduprime.eduprimeserver.repositories.ItemRepository;
import com.eduprime.eduprimeserver.services.ItemService;
import com.eduprime.eduprimeserver.services.LessonService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final LessonService lessonService;

    private final EntityManager manager;

    @Override
    public BaseResponse<?> getListItemByLessonId(String lessonId) {
        Assert.notNull(lessonId, "lessonId not null!");

        var items = this.itemRepository.getListItemByLessonId(lessonId).get();

        var itemDtos = items.stream()
                .map(ItemDto::of)
                .collect(Collectors.toList());

        return BaseResponse.of(itemDtos, HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    @Override
    public BaseResponse<?> getItemById(String itemId) {
        Assert.notNull(itemId, "itemId not null!");

        return BaseResponse.of(ItemDto.of(this.findItemById(itemId)), HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    @Override
    public BaseResponse<?> createItem(ItemRequest request) {
        //validate
        Assert.notNull(request.getLessonId(), "lessonId not null");

        var lesson = this.lessonService.findLessonById(request.getLessonId());

        if (lesson == null) {
            return BaseResponse.error(HttpStatusCodes.NOT_FOUND, HttpStatusMessages.NOT_FOUND +
                    " lesson with lessonId = %s doesw exist.".formatted(request.getLessonId()));
        }

        var item = Item.builder()
                .type(request.getType())
                .title(request.getTitle())
                .content(request.getContent())
                .itemOrder(request.getItemOrder())
                .lesson(lesson)
                .fileType(request.getFileType())
                .fileName(request.getFileName())
                .filePath(request.getFilePath())
                .fileUrl(request.getFileUrl())
                .duration(request.getDuration())
                .image(request.getImage())
                .build();

        item = this.itemRepository.save(item);

        return BaseResponse.of(ItemDto.of(item), HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    @Override
    public BaseResponse<?> updateItem(ItemRequest request, String itemId) {
        //validate
        Assert.notNull(itemId, "itemId not null!");
        Assert.notNull(request.getLessonId(), "lessonId not null");

        var item = this.itemRepository.getItemById(itemId).get();

        if (item == null) {
            return BaseResponse.error(HttpStatusCodes.NOT_FOUND, HttpStatusMessages.NOT_FOUND + "" +
                    " item with itemId = %s does exist.".formatted(itemId));
        }

        var lesson = this.lessonService.findLessonById(request.getLessonId());

        if (lesson == null) {
            return BaseResponse.error(HttpStatusCodes.NOT_FOUND, HttpStatusMessages.NOT_FOUND +
                    " lesson with lessonId = %s does exist.".formatted(request.getLessonId()));
        }

        item.setType(request.getType());
        item.setTitle(request.getTitle());
        item.setContent(request.getContent());
        item.setItemOrder(request.getItemOrder());
        item.setLesson(lesson);
        item.setFileType(request.getFileType());
        item.setFileName(request.getFileName());
        item.setFilePath(request.getFilePath());
        item.setFileUrl(request.getFileUrl());
        item.setDuration(request.getDuration());
        item.setImage(request.getImage());

        item = this.itemRepository.save(item);

        return BaseResponse.of(ItemDto.of(item), HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    @Override
    public BaseResponse<?> deleteItem(String itemId) {
        return null;
    }

    @Override
    public Item findItemById(String itemId) {
        return this.itemRepository.getItemById(itemId).get();
    }

    @Override
    public ResponseEntity<ByteArrayResource> downloadDocument(String documentId) throws IOException {
        Assert.notNull(documentId, "itemId not null! [download document]");

        ItemDto document = (ItemDto) this.getItemById(documentId).getResult();
        String fileName = document.getContent();
        String fileExtension = document.getFileType();
        String savedFileName = String.format("%s.%s", fileName, fileExtension);

        if (!document.getType().equals(ItemType.DOCUMENT.name())) {
            throw new IllegalArgumentException("File type with item = %s is not document [download document]!".formatted(documentId));
        }

        String fileUrl = document.getFilePath();

        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new IllegalArgumentException("File URL is null for documentId: %s".formatted(documentId));
        }

        Path documentPath = Paths.get(fileUrl);

        if (!Files.exists(documentPath)) {
            throw new FileNotFoundException("File not found at path: %s".formatted(documentPath.toString()));
        }

        byte[] documentBytes = Files.readAllBytes(documentPath);

        String encodedFileName = URLEncoder.encode(savedFileName, "UTF-8").replaceAll("\\+", "%20");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", encodedFileName);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(documentBytes.length)
                .body(new ByteArrayResource(documentBytes));
    }


    @Override
    public byte[] viewPdf(String itemId) throws IOException {
        Assert.notNull(itemId, "itemId must not be null!");

        ItemDto item = (ItemDto) this.getItemById(itemId).getResult();

        if (ItemType.DOCUMENT == ItemType.valueOf(item.getType()) && FileType.pdf == FileType.valueOf(item.getFileType())) {
            String fileUrl = item.getFilePath();
            return readPdfContent(fileUrl);
        } else {
            throw new IOException("Item is not a PDF document.");
        }
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
        String sqlCount = "select count(entity.id) from Item as entity ";
        String sql = "select entity from Item as entity ";

//        if (pageRequest.getFromDate() != null) {
//            whereClause += " AND ( entity.createDate >= :fromDate) ";
//        }
//
//        if (pageRequest.getToDate() != null) {
//            whereClause += " AND ( entity.createDate <= :toDate) ";
//        }

        if (pageRequest.getCode() != null) {
            whereClause += " AND ( entity.lesson.id = :lessonId) ";
        }

        sql += whereClause;
        sqlCount += whereClause;

        TypedQuery<Item> q = manager.createQuery(sql, Item.class);
        TypedQuery<Long> qCount = manager.createQuery(sqlCount, Long.class);

//		if (dto.getText() != null && StringUtils.hasText(dto.getText())) {
//			q.setParameter("text", '%' + dto.getText().trim() + '%');
//			qCount.setParameter("text", '%' + dto.getText().trim() + '%');
//		}

//        if (pageRequest.getFromDate() != null) {
//            DateTime dateTime = new DateTime(pageRequest.getFromDate());
//            LocalDateTime fromDate = dateTime.toLocalDateTime();
//            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
//            q.setParameter("fromDate", fromDate);
//            qCount.setParameter("fromDate", fromDate);
//        }
//
//        if (pageRequest.getToDate() != null) {
//            DateTime dateTime = new DateTime(pageRequest.getToDate());
//            LocalDateTime toDate = dateTime.toLocalDateTime();
//            q.setParameter("toDate", toDate);
//            qCount.setParameter("toDate", toDate);
//        }
        if (pageRequest.getCode() != null) {
            q.setParameter("lessonId", pageRequest.getCode());
            qCount.setParameter("lessonId", pageRequest.getCode());
        }

        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);
        List<Item> entities = q.getResultList();
        var itemDto = entities.stream().map(ItemDto::of).collect(Collectors.toList());
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<ItemDto> result = new PageImpl<>(itemDto, pageable, count);

        return BaseResponse.of(result, HttpStatusCodes.OK, HttpStatusMessages.OK);
    }

    private byte[] readPdfContent(String fileUrl) throws IOException {
        Path path = Paths.get(fileUrl);
        return Files.readAllBytes(path);
    }
}
