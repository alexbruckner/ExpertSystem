
package com.bru.jhipster.expertsystem.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for conclusion complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="conclusion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="text" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="question" type="{}question"/>
 *       &lt;/choice>
 *       &lt;attribute name="refId" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="ref" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "conclusion", propOrder = {
    "text",
    "question"
})
public class Conclusion {

    protected String text;
    protected Question question;
    @XmlAttribute(name = "refId")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String refId;
    @XmlAttribute(name = "ref")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Conclusion ref;

    /**
     * Gets the value of the text property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Gets the value of the question property.
     *
     * @return
     *     possible object is
     *     {@link Question }
     *
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * Sets the value of the question property.
     *
     * @param value
     *     allowed object is
     *     {@link Question }
     *
     */
    public void setQuestion(Question value) {
        this.question = value;
    }

    /**
     * Gets the value of the refId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRefId() {
        return refId;
    }

    /**
     * Sets the value of the refId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRefId(String value) {
        this.refId = value;
    }

    /**
     * Gets the value of the ref property.
     *
     * @return
     *     possible object is
     *     {@link Object }
     *
     */
    public Conclusion getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     *
     * @param value
     *     allowed object is
     *     {@link Object }
     *
     */
    public void setRef(Conclusion value) {
        this.ref = value;
    }

}
