package com.bru.jhipster.expertsystem.repository;

import com.bru.jhipster.expertsystem.domain.ExpertSystem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the ExpertSystem entity.
 */
@SuppressWarnings("unused")
public interface ExpertSystemRepository extends JpaRepository<ExpertSystem, Long> {

}
