package br.com.zup.conta.digital.contas;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

public class TransacaoRequest {

    @Positive @NotNull
    private BigDecimal valor;

    @JsonCreator(mode = PROPERTIES)
    public TransacaoRequest(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getValor() {
        return valor;
    }

}
