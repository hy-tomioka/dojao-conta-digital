package br.com.zup.conta.digital.contas.creditar;

import br.com.zup.conta.digital.contas.compartilhado.Conta;

public class CreditaResponse {

    private Long idConta;

    public CreditaResponse(Conta conta) {
        idConta = conta.getId();
   }
}
