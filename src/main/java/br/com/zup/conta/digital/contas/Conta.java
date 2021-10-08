package br.com.zup.conta.digital.contas;

import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Conta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) @NotBlank
    private String numero;

    @NotBlank
    private String idCliente;

    @NotNull
    private BigDecimal saldo;

    @NotNull
    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL)
    private List<Transacao> transacoes = new ArrayList<>();

    @Deprecated
    public Conta() {
    }

    public Conta(String numero, String idCliente) {
        this.numero = numero;
        this.idCliente = idCliente;
        this.saldo = BigDecimal.ZERO;
    }

    public Boolean isDono(String idCliente) {
        return this.idCliente.equalsIgnoreCase(idCliente);
    }

    public void credita(@Positive BigDecimal valor) {
        Assert.isTrue(valor.compareTo(BigDecimal.ZERO) == 1, "Valor a ser creditado na conta deve ser positivo");
        this.saldo = saldo.add(valor);
    }

    public Boolean debita(@Positive BigDecimal valor) {
        Assert.isTrue(valor.compareTo(BigDecimal.ZERO) == 1, "Valor a ser debitado na conta dever ser positivo");
        if(this.saldo.subtract(valor).compareTo(BigDecimal.ZERO) < 0){
            return false;
        }
        this.saldo = this.saldo.subtract(valor);
        return true;
    }

    public void adicionaTransacao(@Valid @NotNull Transacao transacao) {
        this.transacoes.add(transacao);
    }

    public Long getId() {
        return id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public String getNumero() {
        return numero;
    }
}
