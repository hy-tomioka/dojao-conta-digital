package br.com.zup.conta.digital.contas.creditar;

import br.com.zup.conta.digital.contas.Conta;
import br.com.zup.conta.digital.contas.ContaRepository;
import br.com.zup.conta.digital.contas.TransacaoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContaRepository repository;

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    private Conta contaBreno;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        contaBreno = new Conta("1234", UUID.randomUUID().toString());

        entityManager.persist(contaBreno);
    }

    @Test
    void deveCreditarValorNaConta() throws Exception {

        TransacaoRequest body = new TransacaoRequest(BigDecimal.valueOf(10.99));

        MockHttpServletRequestBuilder request = post("/api/v1/clientes/" + contaBreno.getIdCliente() + "/contas/1234/credito")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        Optional<Conta> possivelConta = repository.findById(contaBreno.getId());
        BigDecimal saldo = possivelConta.get().getSaldo();

        assertThat(BigDecimal.valueOf(10.99)).isEqualByComparingTo(saldo);
    }

    @Test
    void deveDebitarValorNaConta() throws Exception {

        contaBreno.credita(BigDecimal.valueOf(40.00));
        TransacaoRequest body = new TransacaoRequest(BigDecimal.valueOf(10.0));

        MockHttpServletRequestBuilder request = post("/api/v1/clientes/" + contaBreno.getIdCliente() + "/contas/1234/debito")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        Optional<Conta> possivelConta = repository.findById(contaBreno.getId());

        BigDecimal saldo = possivelConta.get().getSaldo();

        assertThat(BigDecimal.valueOf(30.0)).isEqualByComparingTo(saldo);

    }

    @Test
    void naoDeveDebitarValorNegativoNaConta() throws Exception {

        TransacaoRequest body = new TransacaoRequest(BigDecimal.valueOf(-10.0));

        MockHttpServletRequestBuilder request = post("/api/v1/clientes/" + contaBreno.getIdCliente() + "/contas/1234/debito")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveDebitarValorMaiorQueSaldoAtual() throws Exception {


        TransacaoRequest body = new TransacaoRequest(BigDecimal.valueOf(10.0));

        MockHttpServletRequestBuilder request = post("/api/v1/clientes/" + contaBreno.getIdCliente() + "/contas/1234/debito")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

}