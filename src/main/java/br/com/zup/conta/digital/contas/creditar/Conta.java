package br.com.zup.conta.digital.contas.creditar;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

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

    public Conta(String numero, Cliente cliente, BigDecimal saldo) {
        this.numero = numero;
        this.cliente = cliente;
        this.saldo = saldo;
    }

    public Boolean isDono(Long idCliente) {
        return (cliente.getId() == idCliente);
    }

    public void credita(BigDecimal valor) {
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
