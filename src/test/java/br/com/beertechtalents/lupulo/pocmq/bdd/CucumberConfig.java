package br.com.beertechtalents.lupulo.pocmq.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@CucumberContextConfiguration
@AutoConfigureMockMvc(addFilters = false)
public class CucumberConfig {
}
