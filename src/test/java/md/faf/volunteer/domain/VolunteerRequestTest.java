package md.faf.volunteer.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import md.faf.volunteer.web.rest.TestUtil;

public class VolunteerRequestTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VolunteerRequest.class);
        VolunteerRequest volunteerRequest1 = new VolunteerRequest();
        volunteerRequest1.setId(1L);
        VolunteerRequest volunteerRequest2 = new VolunteerRequest();
        volunteerRequest2.setId(volunteerRequest1.getId());
        assertThat(volunteerRequest1).isEqualTo(volunteerRequest2);
        volunteerRequest2.setId(2L);
        assertThat(volunteerRequest1).isNotEqualTo(volunteerRequest2);
        volunteerRequest1.setId(null);
        assertThat(volunteerRequest1).isNotEqualTo(volunteerRequest2);
    }
}
