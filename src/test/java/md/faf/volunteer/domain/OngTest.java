package md.faf.volunteer.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import md.faf.volunteer.web.rest.TestUtil;

public class OngTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ong.class);
        Ong ong1 = new Ong();
        ong1.setId(1L);
        Ong ong2 = new Ong();
        ong2.setId(ong1.getId());
        assertThat(ong1).isEqualTo(ong2);
        ong2.setId(2L);
        assertThat(ong1).isNotEqualTo(ong2);
        ong1.setId(null);
        assertThat(ong1).isNotEqualTo(ong2);
    }
}
