package br.com.zup.conta.digital.contas;

import java.math.BigDecimal;

public enum TipoTransacao {
    CREDITO {
        @Override
        boolean efetiva(Conta conta, BigDecimal valor) {
            conta.credita(valor);

            return true;
        }
    },
    DEBITO {
        @Override
        boolean efetiva(Conta conta, BigDecimal valor) {
            return conta.debita(valor);
        }
    };

    abstract boolean efetiva(Conta conta, BigDecimal valor);
}
