package md.faf.volunteer.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A OngRequest.
 */
@Entity
@Table(name = "ong_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OngRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false, unique = true)
    private String name;

    @NotNull
    @Size(max = 13)
    @Column(name = "idno", length = 13, nullable = false, unique = true)
    private String idno;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public OngRequest name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdno() {
        return idno;
    }

    public OngRequest idno(String idno) {
        this.idno = idno;
        return this;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public OngRequest registrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public User getUser() {
        return user;
    }

    public OngRequest user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OngRequest)) {
            return false;
        }
        return id != null && id.equals(((OngRequest) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "OngRequest{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", idno='" + getIdno() + "'" +
            ", registrationDate='" + getRegistrationDate() + "'" +
            "}";
    }
}
