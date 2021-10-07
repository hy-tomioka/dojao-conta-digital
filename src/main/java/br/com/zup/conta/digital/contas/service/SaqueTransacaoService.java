package br.com.zup.conta.digital.contas.service;

import br.com.zup.conta.digital.contas.Conta;
import br.com.zup.conta.digital.contas.Transacao;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SaqueTransacaoService implements TransacaoService {

    @Override
    public void executa(Transacao transacao) {
        Conta conta = transacao.getConta();
        boolean sucesso = conta.debita(transacao.getValor());

        if (!sucesso) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
