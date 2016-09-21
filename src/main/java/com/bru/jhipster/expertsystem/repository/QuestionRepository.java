package com.bru.jhipster.expertsystem.repository;

import com.bru.jhipster.expertsystem.domain.Question;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Question entity.
 */
@SuppressWarnings("unused")
public interface QuestionRepository extends JpaRepository<Question,Long> {

}
