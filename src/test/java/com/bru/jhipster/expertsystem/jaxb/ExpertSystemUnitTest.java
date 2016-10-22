package com.bru.jhipster.expertsystem.jaxb;

import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ExpertSystemUnitTest {
    @Test
    public void unmarshal() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ExpertSystem.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ExpertSystem expertSystem = (ExpertSystem) jaxbUnmarshaller.unmarshal(new File("src/test/resources/loader/BlueBall.xml"));

        String title = expertSystem.getTitle();
        assertThat(title.trim(), is("Blue Ball System"));

        Question q1 = expertSystem.getQuestion();
        assertThat(q1.getText().trim(), is("What color is the ball?"));

        List<Answer> q1Answers = q1.getAnswer();
        assertThat(q1Answers.get(0).getText().trim(), is("Blue."));
        assertThat(q1Answers.get(1).getText().trim(), is("Red."));

        Conclusion q1Answer1Conclusion = q1Answers.get(0).getConclusion();
        assertThat(q1Answer1Conclusion.getText().trim(), is("Blue ball!"));

        assertThat(q1Answers.get(1).getConclusion().getQuestion().getText().trim(), is("Really?"));
    }
}
