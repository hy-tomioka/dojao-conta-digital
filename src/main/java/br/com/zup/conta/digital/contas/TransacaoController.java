package br.com.zup.conta.digital.contas;

import br.com.zup.conta.digital.contas.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/v1/contas")
public class TransacaoController {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private TransacaoService transacaoService;

    @PostMapping("/{numeroConta}")
    public ResponseEntity<TransacaoResponse> transacao(@PathVariable String numeroConta, @Valid @RequestBody TransacaoRequest request) {
        Transacao transacaoFinalizada = transactionTemplate.execute(status -> {
            valida(numeroConta, request.getIdCliente());

            Transacao transacao = request.toTransacao(numeroConta, contaRepository);
            transacaoService.executa(transacao);

            return transacao;
        });

        return ResponseEntity.ok(new TransacaoResponse(transacaoFinalizada));
    }

    private void valida(String numeroConta, String idCliente) {
        contaRepository.findByNumero(numeroConta)
                .ifPresentOrElse(conta -> {
                    if (!conta.isDono(idCliente)) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                    }
                }, () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }
}
