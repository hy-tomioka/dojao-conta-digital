package br.com.zup.conta.digital.contas.creditar;

import br.com.zup.conta.digital.contas.compartilhado.Cliente;
import br.com.zup.conta.digital.contas.compartilhado.Conta;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional

class CreditaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    private Cliente breno;
    private Conta contaBreno;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        breno = new Cliente("Breno");
        contaBreno = new Conta("1234", breno);

        entityManager.persist(breno);
        entityManager.persist(contaBreno);
    }


    @Test
    void deveCreditarValorNaConta() throws Exception {

        CreditaRequest body = new CreditaRequest(breno.getId(), BigDecimal.valueOf(10.99));

        MockHttpServletRequestBuilder request = post("/api/v1/contas/1234/creditar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body));

        mockMvc.perform(request)
                .andExpect(status().isOk());

    }

}