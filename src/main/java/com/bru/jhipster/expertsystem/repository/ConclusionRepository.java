package com.bru.jhipster.expertsystem.repository;

import com.bru.jhipster.expertsystem.domain.Conclusion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Conclusion entity.
 */
@SuppressWarnings("unused")
public interface ConclusionRepository extends JpaRepository<Conclusion,Long> {

}
