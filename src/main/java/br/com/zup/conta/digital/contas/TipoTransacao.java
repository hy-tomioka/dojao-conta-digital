package br.com.zup.conta.digital.contas;

import br.com.zup.conta.digital.contas.service.DepositoTransacaoService;
import br.com.zup.conta.digital.contas.service.NovaTransacaoEvent;
import br.com.zup.conta.digital.contas.service.SaqueTransacaoService;
import br.com.zup.conta.digital.contas.service.TransacaoService;

public enum TipoTransacao implements NovaTransacaoEvent {

    DEPOSITO(new DepositoTransacaoService()),

    SAQUE(new SaqueTransacaoService());

    private final TransacaoService transacaoService;

    TipoTransacao(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    public void executa(Transacao transacao) {
        transacaoService.executa(transacao);
    }
}
