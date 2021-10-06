package br.com.zup.conta.digital.contas;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum TipoTransacao {
    DEPOSITO {
        @Override
        void executa(Transacao transacao) {
            Conta conta = transacao.getConta();
            conta.credita(transacao.getValor());
        }
    },
    SAQUE {
        @Override
        void executa(Transacao transacao) {
            Conta conta = transacao.getConta();
            boolean sucesso = conta.debita(transacao.getValor());

            if (!sucesso) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
    };

    abstract void executa(Transacao transacao);
}
