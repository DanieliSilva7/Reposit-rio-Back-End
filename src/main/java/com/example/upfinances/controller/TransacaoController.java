package com.example.upfinances.controller;

import com.example.upfinances.model.Transacao;
import com.example.upfinances.repository.TransacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {

    private final TransacaoRepository repository;

    // Injeção por construtor: o Spring entrega o repository pronto
    public TransacaoController(TransacaoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Transacao>> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) BigDecimal valorMin,
            @RequestParam(required = false) BigDecimal valorMax) {

        return ResponseEntity.ok(repository.filtrar(tipo, categoria, descricao, valorMin, valorMax));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transacao> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Transacao> cadastrar(@RequestBody Transacao novaTransacao) {
        novaTransacao.setId(null); // garante INSERT: quem gera o ID é o banco
        Transacao salva = repository.save(novaTransacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transacao> atualizar(@PathVariable Long id, @RequestBody Transacao dadosAtualizados) {
        return repository.findById(id)
                .map(existente -> {
                    existente.setDescricao(dadosAtualizados.getDescricao());
                    existente.setValor(dadosAtualizados.getValor());
                    existente.setTipo(dadosAtualizados.getTipo());
                    existente.setCategoria(dadosAtualizados.getCategoria());
                    existente.setData(dadosAtualizados.getData());
                    return ResponseEntity.ok(repository.save(existente));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
