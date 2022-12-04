package red.eminence.commons;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import red.eminence.commons.services.meta.MetaService;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest ("meta.config.message=Falgun!")
class CommonTests
{
    @Autowired
    private MetaService metaService;
    
    @Test
    void contextLoads ()
    {
        assertThat(metaService.getMessage()).isEqualTo("Falgun!");
    }
    
    @SpringBootApplication
    static class TestApplication
    {}
}
