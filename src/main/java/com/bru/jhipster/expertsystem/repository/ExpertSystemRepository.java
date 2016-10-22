package com.bru.jhipster.expertsystem.repository;

import com.bru.jhipster.expertsystem.domain.ExpertSystem;

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
    default ExpertSystem save(String xml) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(com.bru.jhipster.expertsystem.jaxb.ExpertSystem.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        com.bru.jhipster.expertsystem.jaxb.ExpertSystem expertSystemJAXB =
            (com.bru.jhipster.expertsystem.jaxb.ExpertSystem) jaxbUnmarshaller.unmarshal(
                new StringReader(xml)
            );

        ExpertSystem expertSystem = new ExpertSystem().title(expertSystemJAXB.getTitle()).xml(xml);
        return save(expertSystem);
    }

}
