package br.com.zup.conta.digital.contas.compartilhado;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Cliente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Deprecated
    Cliente() {}

    public Cliente(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }
}
