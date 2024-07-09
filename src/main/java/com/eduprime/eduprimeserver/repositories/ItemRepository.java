package com.eduprime.eduprimeserver.repositories;

import com.eduprime.eduprimeserver.domains.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {

    @Query("SELECT i FROM Item i WHERE i.lesson.id = :lessonId")
    Optional<List<Item>> getListItemByLessonId(@Param("lessonId") String lessonId);

    @Query("SELECT i FROM Item i WHERE i.id = :itemId")
    Optional<Item> getItemById(@Param("itemId") String itemId);
}
