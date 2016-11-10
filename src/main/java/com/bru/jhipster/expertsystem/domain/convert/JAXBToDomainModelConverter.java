package com.bru.jhipster.expertsystem.domain.convert;

import com.bru.jhipster.expertsystem.domain.Answer;
import com.bru.jhipster.expertsystem.domain.Conclusion;
import com.bru.jhipster.expertsystem.domain.ExpertSystem;
import com.bru.jhipster.expertsystem.domain.Question;
import com.bru.jhipster.expertsystem.repository.AnswerRepository;
import com.bru.jhipster.expertsystem.repository.ConclusionRepository;
import com.bru.jhipster.expertsystem.repository.ExpertSystemRepository;
import com.bru.jhipster.expertsystem.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by bral on 23.10.2016.
 */
@Service
public class JAXBToDomainModelConverter {

    @Inject
    private ExpertSystemRepository expertSystemRepository;

    @Inject
    private QuestionRepository questionRepository;

    @Inject
    private AnswerRepository answerRepository;

    @Inject
    private ConclusionRepository conclusionRepository;

    public ExpertSystem convertAndSave(String xml) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(com.bru.jhipster.expertsystem.jaxb.ExpertSystem.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        com.bru.jhipster.expertsystem.jaxb.ExpertSystem expertSystemJAXB =
            (com.bru.jhipster.expertsystem.jaxb.ExpertSystem) jaxbUnmarshaller.unmarshal(
                new StringReader(xml)
            );

        ExpertSystem expertSystem = new ExpertSystem()
            .title(expertSystemJAXB.getTitle().trim())
            .xml(xml).question(convert(expertSystemJAXB.getQuestion(), new HashMap<>()));

        return expertSystemRepository.save(expertSystem);
    }

    private Question convert(com.bru.jhipster.expertsystem.jaxb.Question questionJAXB, HashMap<String, Conclusion> referencedConclusions) {
        if (questionJAXB == null) return null;
        Set<Answer> answers = new HashSet<>();
        Question question = new Question().text(questionJAXB.getText());
        question = questionRepository.save(question);
        for (com.bru.jhipster.expertsystem.jaxb.Answer answer : questionJAXB.getAnswer()) {
            answers.add(convert(answer, question, referencedConclusions));
        }
        question.answers(answers);
        return questionRepository.save(question);
    }

    private Answer convert(com.bru.jhipster.expertsystem.jaxb.Answer answerJAXB, Question question, HashMap<String, Conclusion> referencedConclusions) {
        if (answerJAXB == null) return null;
        Answer answer = new Answer().text(answerJAXB.getText()).conclusion(convert(answerJAXB.getConclusion(), referencedConclusions));
        answer.setQuestion(question);
        return answerRepository.save(answer);
    }

    private Conclusion convert(com.bru.jhipster.expertsystem.jaxb.Conclusion conclusionJAXB, HashMap<String, Conclusion> referencedConclusions) {
        if (conclusionJAXB == null) return null;

        if (conclusionJAXB.getRef() != null) {
            return referencedConclusions.get(conclusionJAXB.getRef().getRefId());
        }

        Conclusion conclusion = new Conclusion().text(conclusionJAXB.getText()).question(convert(conclusionJAXB.getQuestion(), referencedConclusions));
        conclusion = conclusionRepository.save(conclusion);

        if (conclusionJAXB.getRefId() != null) {
            referencedConclusions.put(conclusionJAXB.getRefId(), conclusion);
        }

        return conclusion;
    }
}
