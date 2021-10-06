package br.com.zup.conta.digital.contas;

import java.math.BigDecimal;
import java.util.UUID;

public class TransacaoResponse {

    private final Long idConta;
    private final String idCliente;
    private final UUID idTransacao;
    private final BigDecimal valor;
    private final TipoTransacao tipoTransacao;

    public TransacaoResponse(Transacao transacao) {
        this.idConta = transacao.getConta().getId();
        this.idCliente = transacao.getConta().getIdCliente();
        this.idTransacao = transacao.getUuid();
        this.valor = transacao.getValor();
        this.tipoTransacao = transacao.getTipoTransacao();
   }

    public Long getIdConta() {
        return idConta;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public UUID getIdTransacao() {
        return idTransacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public TipoTransacao getTipoTransacao() {
        return tipoTransacao;
    }
}
