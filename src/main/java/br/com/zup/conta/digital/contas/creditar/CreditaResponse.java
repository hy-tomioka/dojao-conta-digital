package br.com.zup.conta.digital.contas.creditar;

import java.math.BigDecimal;

public class CreditaResponse {

    private Long idConta;

    public CreditaResponse(Conta conta) {
        idConta = conta.getId();
   }
}
