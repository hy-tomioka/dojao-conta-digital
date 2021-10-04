package br.com.zup.conta.digital.contas.compartilhado;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Cliente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Deprecated
    Cliente() {}

    public Long getId() {
        return id;
    }
}
