package br.com.zup.conta.digital.contas.service;

import br.com.zup.conta.digital.contas.Transacao;

public interface NovaTransacaoEvent {

    void executa(Transacao transacao);
}
