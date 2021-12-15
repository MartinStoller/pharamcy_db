package de.haegerconsulting.training.martin.apotheke_stoller.bestellung;

import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "bestellungen")
@Validated // Tells spring to validate the variables and Parameters of this class (works together with e.g. @Min and @max where the allowed values are defined) (Need to be put on a class!)
public class Bestellung {
    @Id
    //Sequence Generator generates automatically new Ids for each Order
    @SequenceGenerator(
            name = "bestellung_sequence",
            sequenceName = "bestellung_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator =  "bestellung_sequence"
    )

    private Long id;

    @NotNull
    @Min(10000000)
    @Max(99999999)
    private Long product_id;

    @Min(value = 0, message = "Ordervolume must be at least a Natural Number")  // THis is a validation annotation
    private int amount;

    @NotNull
    private int store_id;  //in this hypothetical setting, stores are registered by ID, and all further details about the store such as the address can be derived from that

    @Min(value = 0, message = "Status needs to be a Number between 0 and 4!")
    @Max(value = 4, message = "Status needs to be a Number between 0 and 4!")
    private int status;  // 0=order not yet processed, 1=order has been sent, but not database was not yet adjusted, 2=order is fully processed and finished, 4=canceled

    @NotNull
    private LocalDate date;

    // Constructors:
    public Bestellung() {
    }

    public Bestellung(Long product_id, int amount, int store_id, int status, LocalDate date) {
        //no Bestellung_id because it will be generated by our db sequence generator
        this.product_id = product_id;
        this.amount = amount;
        this.store_id = store_id;
        this.status = status;
        this.date = date;
    }

    //Getter and setter:
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Bestellung{" +
                "id=" + id +
                ", product_id=" + product_id +
                ", amount=" + amount +
                ", store_id=" + store_id +
                ", status=" + status +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bestellung)) return false;
        Bestellung that = (Bestellung) o;
        return getAmount() == that.getAmount() && getStore_id() == that.getStore_id() && getStatus() == that.getStatus() && Objects.equals(getId(), that.getId()) && Objects.equals(getProduct_id(), that.getProduct_id()) && Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getProduct_id(), getAmount(), getStore_id(), getStatus(), getDate());
    }
}
