package br.com.zup.conta.digital.contas;

import br.com.zup.conta.digital.contas.service.TransacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ActiveProfiles("mysql")
//@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
public class ConsistenciaSaldoTest {

    private static final int PARALELISMO = 4;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private Conta contaBreno;

//    @Container
//    private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer("mysql:8.0.26");
//
//    @DynamicPropertySource
//    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
//        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
//        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
//    }

    @BeforeEach
    void setUp() {
        transactionTemplate.execute(status -> {
            contaBreno = new Conta("1234", UUID.randomUUID().toString());
            contaBreno.credita(BigDecimal.valueOf(39.0));

            return contaRepository.save(contaBreno);
        });
    }

    @AfterEach
    void tearDown() {
        transactionTemplate.execute(status -> {
            contaRepository.deleteAll();
            return status;
        });
    }

    @Test
    void deveManterValorSaldoConsistente() throws Exception {

        TransacaoRequest body = new TransacaoRequest(contaBreno.getIdCliente(), BigDecimal.valueOf(6.25), TipoTransacao.SAQUE);

        URI uri = URI.create(String.format("/api/v1/contas/%s", contaBreno.getNumero()));

        MockHttpServletRequestBuilder request = post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body));

        Callable<AssertionError> task = () -> {
            try {
                mockMvc.perform(request)
                        .andExpect(status().isOk());
                return null;
            } catch (AssertionError e) {
                return e;
            }
        };

        List<Callable<AssertionError>> taskList = Collections.nCopies(PARALELISMO, task);

        ExecutorService executorService = Executors.newFixedThreadPool(PARALELISMO);
        List<Future<AssertionError>> resultadoList = executorService.invokeAll(taskList);

        for (Future<AssertionError> future : resultadoList) {
            if (future.get() != null) {
                throw future.get();
            }
        }
        executorService.shutdown();

        BigDecimal saldo = transactionTemplate.execute(status -> contaRepository.findByNumero("1234").get().getSaldo());

        assertEquals(BigDecimal.TEN.compareTo(saldo), 0);

    }


    @TestConfiguration
    public static class TransacaoServiceFactory {

        @Bean
        @Primary
        public TransacaoService transacaoSleepService() {
            return transacao -> {

                System.out.println("Utilizando implementacao de teste do serviço de transacao");

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Conta conta = transacao.getConta();
                boolean sucesso = conta.debita(transacao.getValor().add(BigDecimal.ONE));

                if (!sucesso) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
                }
            };
        }
    }

}
