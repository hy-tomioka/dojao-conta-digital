package br.com.zup.conta.digital.contas;

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
import java.math.BigDecimal;
import java.util.Optional;

import static br.com.zup.conta.digital.contas.TipoTransacao.CREDITO;
import static br.com.zup.conta.digital.contas.TipoTransacao.DEBITO;

@RestController
@Validated
public class TransacaoController {

    @Autowired
    ContaRepository contaRepository;

    @Autowired
    TransactionTemplate transactionTemplate;

    @PostMapping("api/v1/clientes/{idCliente}/contas/{numeroConta}/credito")
    public ResponseEntity<TransacaoResponse> creditar(
            @PathVariable String idCliente,
            @PathVariable String numeroConta,
            @Valid @RequestBody TransacaoRequest request) {
        return transacao(idCliente, numeroConta, request, CREDITO);
    }

    @PostMapping("api/v1/clientes/{idCliente}/contas/{numeroConta}/debito")
    public ResponseEntity<TransacaoResponse> debitar(
            @PathVariable String idCliente,
            @PathVariable String numeroConta,
            @Valid @RequestBody TransacaoRequest request) {
        return transacao(idCliente, numeroConta, request, DEBITO);
    }

    private ResponseEntity<TransacaoResponse> transacao(String idCliente, String numeroConta,
                                                        TransacaoRequest request, TipoTransacao tipoTransacao) {
        return transactionTemplate.execute(status -> {

            Optional<Conta> possivelConta = contaRepository.findByNumero(numeroConta);

            if (possivelConta.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Conta conta = possivelConta.get();
            if (!conta.isDono(idCliente)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            BigDecimal valor = request.getValor();
            boolean efetivada = tipoTransacao.efetiva(conta, valor);

            if (!efetivada) {
                return ResponseEntity.unprocessableEntity().build();
            }

            return ResponseEntity.ok(new TransacaoResponse(conta, valor));
        });
    }
}
