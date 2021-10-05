package br.com.zup.conta.digital.contas;

import java.math.BigDecimal;

public class TransacaoResponse {

    private Long idConta;
    private String idCliente;
    private BigDecimal valor;

    public TransacaoResponse(Conta conta, BigDecimal valor) {
        idConta = conta.getId();
        idCliente = conta.getIdCliente();
        this.valor = valor;
   }

    public Long getIdConta() {
        return idConta;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
