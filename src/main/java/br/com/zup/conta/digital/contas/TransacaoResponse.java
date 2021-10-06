package br.com.zup.conta.digital.contas;

import java.math.BigDecimal;
import java.util.UUID;

public class TransacaoResponse {

    private final String numeroConta;
    private final String idCliente;
    private final UUID idTransacao;
    private final BigDecimal valor;
    private final TipoTransacao tipoTransacao;

    public TransacaoResponse(Transacao transacao) {
        this.numeroConta = transacao.getConta().getNumero();
        this.idCliente = transacao.getConta().getIdCliente();
        this.idTransacao = transacao.getUuid();
        this.valor = transacao.getValor();
        this.tipoTransacao = transacao.getTipoTransacao();
   }

    public String getNumeroConta() {
        return numeroConta;
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
