package com.bru.jhipster.expertsystem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ExpertSystem.
 */
@Entity
@Table(name = "expert_system")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExpertSystem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "xml", nullable = false)
    @Lob
    private String xml;

    @OneToOne
    @JoinColumn(unique = true)
    private Question question;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public ExpertSystem title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getXml() {
        return xml;
    }

    public ExpertSystem xml(String xml) {
        this.xml = xml;
        return this;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public Question getQuestion() {
        return question;
    }

    public ExpertSystem question(Question question) {
        this.question = question;
        return this;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExpertSystem expertSystem = (ExpertSystem) o;
        if(expertSystem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, expertSystem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExpertSystem{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", xml='" + xml + "'" +
            '}';
    }
}
