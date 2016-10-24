package com.bru.jhipster.expertsystem.repository;

import com.bru.jhipster.expertsystem.domain.ExpertSystem;

import com.bru.jhipster.expertsystem.domain.Question;
import com.bru.jhipster.expertsystem.domain.convert.JAXBToDomainModelConverter;
import com.bru.jhipster.expertsystem.jaxb.Answer;
import org.springframework.data.jpa.repository.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.List;

/**
 * Spring Data JPA repository for the ExpertSystem entity.
 */
@SuppressWarnings("unused")
public interface ExpertSystemRepository extends JpaRepository<ExpertSystem,Long> {

}
