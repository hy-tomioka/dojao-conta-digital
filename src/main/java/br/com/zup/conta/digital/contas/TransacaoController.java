package br.com.zup.conta.digital.contas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/v1/clientes/{idCliente}")
public class TransacaoController {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @PostMapping("/transacoes")
    public ResponseEntity<TransacaoResponse> transacao(@PathVariable String idCliente, @Valid @RequestBody TransacaoRequest request) {
        return transacao(idCliente, request, request.getTipoTransacao());
    }

    public ResponseEntity<TransacaoResponse> transacao(String idCliente, TransacaoRequest request, TipoTransacao tipoTransacao) {
        Transacao transacaoFinalizada = transactionTemplate.execute(status -> {
            valida(request, idCliente);

            Transacao transacao = request.toTransacao(tipoTransacao, contaRepository);
            tipoTransacao.executa(transacao);

            return transacao;
        });

        return ResponseEntity.ok(new TransacaoResponse(transacaoFinalizada));
    }

    private void valida(TransacaoRequest request, String idCliente) {
        contaRepository.findByNumero(request.getNumeroConta())
                .ifPresentOrElse(conta -> {
                    if (!conta.isDono(idCliente)) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                    }
                }, () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }
}
