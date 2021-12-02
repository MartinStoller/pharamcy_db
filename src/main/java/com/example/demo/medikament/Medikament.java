package com.example.demo.medikament;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity //maps Medikament to our database (it allows hibernate to map this object to our db)
@Table(name="medikamente") // this allows the mapping to our specific table in the db
public class Medikament {
    @Id //@Id annotation specifies the primary key of an entity (entities need identifiers -> always needed!)
    private int id;  // Pharmazentralnummer
    private String name;
    private String wirkstoff;
    private String hersteller;
    private int vorrat;

    public Medikament() {  //empty Constructor (not sure yet if I´ll need it)
    }

    public Medikament(int id, String name, String wirkstoff, String hersteller, int vorrat) {
        this.id = id;
        this.name = name;
        this.wirkstoff = wirkstoff;
        this.hersteller = hersteller;
        this.vorrat = vorrat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
