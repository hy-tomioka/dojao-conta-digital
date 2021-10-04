package br.com.zup.conta.digital.contas.compartilhado;

import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Conta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) @NotBlank
    private String numero;

    @NotNull
    @ManyToOne
    private Cliente cliente;

    @NotNull
    private BigDecimal saldo;

    @Deprecated
    public Conta() {
    }

    public Conta(String numero, Cliente cliente) {
        this.numero = numero;
        this.cliente = cliente;
        this.saldo = BigDecimal.ZERO;
    }

    public Boolean isDono(Long idCliente) {
        return Objects.equals(cliente.getId(), idCliente);
    }

    public void credita(BigDecimal valor) {
        Assert.isTrue(valor.compareTo(BigDecimal.ZERO) == 1, "Valor a ser creditado na conta deve ser positivo");
        this.saldo = saldo.add(valor);
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
}
