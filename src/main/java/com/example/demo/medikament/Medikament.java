package com.example.demo.medikament;

import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity //maps Medikament to our database (it allows hibernate to map this object to our db)
@Table(name="medikamente") // this allows the mapping to our specific table in the db
@Validated
public class Medikament {
    @Id //@Id annotation specifies the primary key of an entity (entities need identifiers -> always needed!)
    @Size(min=8, max=8, message = "ID must have 8 characters")
    private Long id;  // Pharmazentralnummer
    private String name;
    private String wirkstoff;
    private String hersteller;
    @Min(value=0, message = "Vorrat can not be negative")
    private int vorrat;

    public Medikament() {  //empty Constructor ... needed fot hibernate
    }

    public Medikament(Long id, String name, String wirkstoff, String hersteller, int vorrat) {
        this.id = id;
        this.name = name;
        this.wirkstoff = wirkstoff;
        this.hersteller = hersteller;
        this.vorrat = vorrat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWirkstoff() {
        return wirkstoff;
    }

    public void setWirkstoff(String wirkstoff) {
        this.wirkstoff = wirkstoff;
    }

    public String getHersteller() {
        return hersteller;
    }

    public void setHersteller(String hersteller) {
        this.hersteller = hersteller;
    }

    public int getVorrat() {
        return vorrat;
    }

    public void setVorrat(int vorrat) {
        this.vorrat = vorrat;
    }

    @Override
    public String toString() {
        return "Medikament{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", wirkstoff='" + wirkstoff + '\'' +
                ", hersteller='" + hersteller + '\'' +
                ", vorrat=" + vorrat +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medikament)) return false;
        Medikament that = (Medikament) o;
        return getVorrat() == that.getVorrat() && Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName()) && Objects.equals(getWirkstoff(), that.getWirkstoff()) && Objects.equals(getHersteller(), that.getHersteller());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getWirkstoff(), getHersteller(), getVorrat());
    }
}
