package br.com.zup.conta.digital.contas;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

public class TransacaoRequest {

    @NotBlank
    private String numeroConta;

    @Positive @NotNull
    private BigDecimal valor;

    @NotNull
    private TipoTransacao tipoTransacao;

    @JsonCreator(mode = PROPERTIES)
    public TransacaoRequest(String numeroConta, BigDecimal valor, TipoTransacao tipoTransacao) {
        this.numeroConta = numeroConta;
        this.valor = valor;
        this.tipoTransacao = tipoTransacao;
    }

    public Transacao toTransacao(TipoTransacao tipoTransacao, ContaRepository contaRepository) {

        Conta conta = contaRepository.findByNumero(numeroConta)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return new Transacao(valor, tipoTransacao, conta);
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public TipoTransacao getTipoTransacao() {
        return tipoTransacao;
    }
}
