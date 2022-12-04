package red.eminence.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import red.eminence.commons.services.meta.MetaService;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest ("meta.config.message=Falgun!")
		// @ComponentScan (basePackages = {"red.eminence.user", "red.eminence.commons"})
		// @EnableWebFlux
class UserApplicationTests
{
	@Autowired
	private MetaService                          metaService;
	@Autowired
	private red.eminence.user.services.auth.Test test;
	
	@Test
	void contextLoads ()
	{
		System.out.println(test.getMessage());
		assertThat(metaService.getMessage()).isEqualTo("Falgun!");
	}
	//	@SpringBootApplication
	//	static class TestApplication
	//	{}
}
