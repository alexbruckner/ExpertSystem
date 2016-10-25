package com.bru.jhipster.expertsystem.repository;

import com.bru.jhipster.expertsystem.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Answer entity.
 */
@SuppressWarnings("unused")
public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
