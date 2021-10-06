package br.com.zup.conta.digital.contas;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ContaTest {

    @Test
    void naoDeveRealizarDebitoComSaldoInsuficiente() {
        Conta conta = new Conta("1234", UUID.randomUUID().toString());
        assertFalse(conta.debita(BigDecimal.TEN));
    }
}