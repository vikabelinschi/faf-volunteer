package md.faf.volunteer.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import md.faf.volunteer.web.rest.TestUtil;

public class OngRequestTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OngRequest.class);
        OngRequest ongRequest1 = new OngRequest();
        ongRequest1.setId(1L);
        OngRequest ongRequest2 = new OngRequest();
        ongRequest2.setId(ongRequest1.getId());
        assertThat(ongRequest1).isEqualTo(ongRequest2);
        ongRequest2.setId(2L);
        assertThat(ongRequest1).isNotEqualTo(ongRequest2);
        ongRequest1.setId(null);
        assertThat(ongRequest1).isNotEqualTo(ongRequest2);
    }
}
