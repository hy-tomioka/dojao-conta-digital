package br.com.zup.conta.digital.contas;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
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

@SpringBootTest
@AutoConfigureMockMvc
public class TransacoesSimultaneasTest {

    private static final int PARALELISMO = 8;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private Conta contaBreno;

    @BeforeEach
    void setUp() {
        transactionTemplate.execute(status -> {
            contaBreno = new Conta("1234", UUID.randomUUID().toString());
            contaBreno.credita(BigDecimal.valueOf(52.0));

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
    void naoDevePermitirAExecucaoDeMultiplasOperacoesDeDebitoSimultaneas() throws Exception {

        TransacaoRequest body = new TransacaoRequest(BigDecimal.valueOf(6.25));

        MockHttpServletRequestBuilder request = post("/api/v1/clientes/" + contaBreno.getIdCliente() + "/contas/1234/debito")
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

        assertEquals(BigDecimal.valueOf(2.0).compareTo(saldo), 0);
    }
}
