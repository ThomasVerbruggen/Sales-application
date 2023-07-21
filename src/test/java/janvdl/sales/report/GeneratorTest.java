package janvdl.sales.report;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@SpringBootTest
@ActiveProfiles("test")

class GeneratorTest {

	@Autowired
	private Generator generator;

	@Test
	void generatorTest() {

		LocalDateTime fromDate = LocalDateTime.of(2017, 6, 1, 0, 0, 0);
		LocalDateTime toDate = LocalDateTime.of(2017, 6, 30, 23, 59, 59);

		generator.create(15 * 60, fromDate, toDate);

		generator.stream().forEach(e -> System.out.println(e.getKey()
				+ " -> " + e.getValue()));

		System.out.println("Total :" + generator.totalSum());

	}

}
