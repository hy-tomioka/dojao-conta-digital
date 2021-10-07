package br.com.zup.conta.digital.contas.service;

import br.com.zup.conta.digital.contas.Transacao;
import org.springframework.stereotype.Component;

@Component
public class NovaTransacaoService implements TransacaoService {

    public void executa(Transacao transacao) {
        transacao.getTipoTransacao().executa(transacao);
    }
}
