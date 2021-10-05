package br.com.zup.conta.digital.contas.debitar;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

public class DebitaRequest {
    @Positive
    @NotNull
    private BigDecimal valor;

    @JsonCreator(mode = PROPERTIES)
    public DebitaRequest(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
