package red.eminence.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import red.eminence.commons.services.meta.MetaService;
import red.eminence.user.services.TestComponent;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest ("meta.config.message=Falgun!")
class UserApplicationTests
{
    @Autowired
    private MetaService   metaService;
    @Autowired
    private TestComponent testComponent;
    
    @Test
    void contextLoads ()
    {
        assertThat(metaService.getMessage()).isEqualTo("Falgun!");  // test @Autowired from commons
        assertThat(testComponent.getMessage()).isEqualTo("Falgun!"); // test @RequiredArgsConstructor
    }
}
