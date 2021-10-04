package br.com.zup.conta.digital.contas.creditar;

import br.com.zup.conta.digital.contas.compartilhado.Conta;
import br.com.zup.conta.digital.contas.compartilhado.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@Validated
public class CreditaController {

    @Autowired
    ContaRepository contaRepository;

    @Autowired
    TransactionTemplate transactionTemplate;

    @PostMapping("api/v1/contas/{numero}/creditar")
    public ResponseEntity<CreditaResponse> creditar(@PathVariable String numero, @Valid @RequestBody CreditaRequest request) {

        return transactionTemplate.execute(status -> {

            Optional<Conta> possivelConta = contaRepository.findByNumero(numero);

            if (possivelConta.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Conta conta = possivelConta.get();
            if (!conta.isDono(request.getIdCliente())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            conta.credita(request.getValor());

            return ResponseEntity.ok(new CreditaResponse(conta));
        });
    }
}
