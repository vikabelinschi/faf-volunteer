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
 * Criteria class for the {@link md.faf.volunteer.domain.VolunteerRequest} entity. This class is used
 * in {@link md.faf.volunteer.web.rest.VolunteerRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /volunteer-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VolunteerRequestCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter registrationDate;

    private LongFilter userId;

    private LongFilter projectId;

    public VolunteerRequestCriteria(){
    }

    public VolunteerRequestCriteria(VolunteerRequestCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.registrationDate = other.registrationDate == null ? null : other.registrationDate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
    }

    @Override
    public VolunteerRequestCriteria copy() {
        return new VolunteerRequestCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateFilter registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final VolunteerRequestCriteria that = (VolunteerRequestCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(registrationDate, that.registrationDate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        registrationDate,
        userId,
        projectId
        );
    }

    @Override
    public String toString() {
        return "VolunteerRequestCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (registrationDate != null ? "registrationDate=" + registrationDate + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (projectId != null ? "projectId=" + projectId + ", " : "") +
            "}";
    }

}
