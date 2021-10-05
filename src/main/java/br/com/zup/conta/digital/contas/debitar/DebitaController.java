package br.com.zup.conta.digital.contas.debitar;

import br.com.zup.conta.digital.contas.compartilhado.Conta;
import br.com.zup.conta.digital.contas.compartilhado.ContaRepository;
import br.com.zup.conta.digital.contas.creditar.CreditaRequest;
import br.com.zup.conta.digital.contas.creditar.CreditaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class DebitaController {

    @Autowired
    ContaRepository contaRepository;

    @Autowired
    TransactionTemplate transactionTemplate;

    @PostMapping("api/v1/clientes/{idCliente}/contas/{numeroConta}/debito")
    public ResponseEntity<DebitaResponse> debitar(@PathVariable String idCliente, @PathVariable String numeroConta, @Valid @RequestBody DebitaRequest request) {

        return transactionTemplate.execute(status -> {

            Optional<Conta> possivelConta = contaRepository.findByNumero(numeroConta);

            if (possivelConta.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Conta conta = possivelConta.get();
            if (!conta.isDono(idCliente)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            conta.debita(request.getValor());

            return ResponseEntity.ok(new DebitaResponse(conta, request.getValor()));
        });
    }
}
