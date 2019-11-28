package md.faf.volunteer.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link md.faf.volunteer.domain.Ong} entity. This class is used
 * in {@link md.faf.volunteer.web.rest.OngResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ongs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OngCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter idno;

    private StringFilter email;

    private StringFilter address;

    private StringFilter phone;

    private LongFilter projectId;

    public OngCriteria(){
    }

    public OngCriteria(OngCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.idno = other.idno == null ? null : other.idno.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
    }

    @Override
    public OngCriteria copy() {
        return new OngCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getIdno() {
        return idno;
    }

    public void setIdno(StringFilter idno) {
        this.idno = idno;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OngCriteria that = (OngCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(idno, that.idno) &&
            Objects.equals(email, that.email) &&
            Objects.equals(address, that.address) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        description,
        idno,
        email,
        address,
        phone,
        projectId
        );
    }

    @Override
    public String toString() {
        return "OngCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (idno != null ? "idno=" + idno + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (projectId != null ? "projectId=" + projectId + ", " : "") +
            "}";
    }

}
