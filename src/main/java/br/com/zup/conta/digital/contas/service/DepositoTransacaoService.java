package br.com.zup.conta.digital.contas.service;

import br.com.zup.conta.digital.contas.Conta;
import br.com.zup.conta.digital.contas.Transacao;

public class DepositoTransacaoService implements TransacaoService {

    @Override
    public void executa(Transacao transacao) {
        Conta conta = transacao.getConta();
        conta.credita(transacao.getValor());
    }
}
