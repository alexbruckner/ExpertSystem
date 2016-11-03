package com.bru.jhipster.expertsystem.repository;

import com.bru.jhipster.expertsystem.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Answer entity.
 */
@SuppressWarnings("unused")
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestion_id(Long id);
}
