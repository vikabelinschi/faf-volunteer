package md.faf.volunteer.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Ong.
 */
@Entity
@Table(name = "ong")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Ong implements Serializable {

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
    @Size(max = 5000)
    @Column(name = "description", length = 5000, nullable = false)
    private String description;

    @NotNull
    @Size(max = 13)
    @Column(name = "idno", length = 13, nullable = false, unique = true)
    private String idno;

    @Size(max = 50)
    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Size(max = 50)
    @Column(name = "address", length = 50)
    private String address;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @OneToMany(mappedBy = "ong")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Project> projects = new HashSet<>();

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

    public Ong name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Ong description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdno() {
        return idno;
    }

    public Ong idno(String idno) {
        this.idno = idno;
        return this;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getEmail() {
        return email;
    }

    public Ong email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public Ong address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public Ong phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public Ong projects(Set<Project> projects) {
        this.projects = projects;
        return this;
    }

    public Ong addProject(Project project) {
        this.projects.add(project);
        project.setOng(this);
        return this;
    }

    public Ong removeProject(Project project) {
        this.projects.remove(project);
        project.setOng(null);
        return this;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ong)) {
            return false;
        }
        return id != null && id.equals(((Ong) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Ong{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", idno='" + getIdno() + "'" +
            ", email='" + getEmail() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
