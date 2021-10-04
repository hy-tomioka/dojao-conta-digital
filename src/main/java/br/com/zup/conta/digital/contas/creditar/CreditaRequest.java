package br.com.zup.conta.digital.contas.creditar;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

public class CreditaRequest {

    @NotNull
    private Long idCliente;

    @Positive @NotNull
    private BigDecimal valor;

    @JsonCreator(mode = PROPERTIES)
    public CreditaRequest(Long idCliente, BigDecimal valor) {
        this.idCliente = idCliente;
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Long getIdCliente() {
        return idCliente;
    }
}
