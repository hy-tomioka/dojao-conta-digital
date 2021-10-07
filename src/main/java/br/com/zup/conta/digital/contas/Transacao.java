package br.com.zup.conta.digital.contas;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
public class Transacao {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true)
    private UUID uuid = UUID.randomUUID();

    @Positive
    private final BigDecimal valor;

    @NotNull
    @Enumerated(EnumType.STRING)
    private final TipoTransacao tipoTransacao;

    private Instant criadoEm = Instant.now();

    @ManyToOne(optional = false)
    private Conta conta;

    public Transacao(BigDecimal valor, TipoTransacao tipoTransacao, Conta conta) {
        this.valor = valor;
        this.tipoTransacao = tipoTransacao;
        this.conta = conta;
    }

    public UUID getUuid() {
        return uuid;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public TipoTransacao getTipoTransacao() {
        return tipoTransacao;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Conta getConta() {
        return conta;
    }
}
