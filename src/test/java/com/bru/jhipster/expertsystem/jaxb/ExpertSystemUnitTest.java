package com.bru.jhipster.expertsystem.jaxb;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

public class ExpertSystemUnitTest {
    @Test
    public void marshal() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ExpertSystem.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ExpertSystem expertSystem = (ExpertSystem) jaxbUnmarshaller.unmarshal(new File("src/test/resources/loader/BlueBall.xml"));

        Question q1 = expertSystem.getQuestion();
        Assert.assertThat(q1.getText().trim(), Is.is("What color is the ball?"));

        List<Answer> q1Answers = q1.getAnswer();
        Assert.assertThat(q1Answers.get(0).getText().trim(), Is.is("Blue."));
        Assert.assertThat(q1Answers.get(1).getText().trim(), Is.is("Red."));

        Conclusion q1Answer1Conclusion = q1Answers.get(0).getConclusion();
        Assert.assertThat(q1Answer1Conclusion.getText().trim(), Is.is("Blue ball!"));
    }
}
