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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link md.faf.volunteer.domain.Project} entity. This class is used
 * in {@link md.faf.volunteer.web.rest.ProjectResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /projects?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProjectCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private LocalDateFilter registrationDate;

    private LongFilter statusId;

    private LongFilter categoryId;

    private LongFilter userId;

    private LongFilter ongId;

    public ProjectCriteria(){
    }

    public ProjectCriteria(ProjectCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.registrationDate = other.registrationDate == null ? null : other.registrationDate.copy();
        this.statusId = other.statusId == null ? null : other.statusId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.ongId = other.ongId == null ? null : other.ongId.copy();
    }

    @Override
    public ProjectCriteria copy() {
        return new ProjectCriteria(this);
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

    public LocalDateFilter getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateFilter registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LongFilter getStatusId() {
        return statusId;
    }

    public void setStatusId(LongFilter statusId) {
        this.statusId = statusId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getOngId() {
        return ongId;
    }

    public void setOngId(LongFilter ongId) {
        this.ongId = ongId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProjectCriteria that = (ProjectCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(registrationDate, that.registrationDate) &&
            Objects.equals(statusId, that.statusId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(ongId, that.ongId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        description,
        registrationDate,
        statusId,
        categoryId,
        userId,
        ongId
        );
    }

    @Override
    public String toString() {
        return "ProjectCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (registrationDate != null ? "registrationDate=" + registrationDate + ", " : "") +
                (statusId != null ? "statusId=" + statusId + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (ongId != null ? "ongId=" + ongId + ", " : "") +
            "}";
    }

}
