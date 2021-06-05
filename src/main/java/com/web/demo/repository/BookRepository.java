package com.web.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.demo.domain.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

}
