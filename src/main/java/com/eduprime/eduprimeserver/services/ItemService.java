package com.eduprime.eduprimeserver.services;

import com.eduprime.eduprimeserver.domains.Item;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.request.ItemRequest;
import com.eduprime.eduprimeserver.dtos.request.PageReq;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ItemService {

    BaseResponse<?> getListItemByLessonId(String lessonId);

    BaseResponse<?> getItemById(String itemId);

    BaseResponse<?> createItem(ItemRequest request);

    BaseResponse<?> updateItem(ItemRequest request, String itemId);

    BaseResponse<?> deleteItem(String itemId);

    Item findItemById(String itemId);

    ResponseEntity<ByteArrayResource> downloadDocument(String documentId) throws IOException;

    byte[] viewPdf(String itemId) throws IOException;

    BaseResponse<Page> paging(PageReq pageRequest);
}
