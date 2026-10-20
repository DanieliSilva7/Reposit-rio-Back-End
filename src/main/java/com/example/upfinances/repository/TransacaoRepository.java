package com.example.upfinances.repository;

import com.example.upfinances.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    // Cada filtro só vale se foi informado (parâmetro não nulo).
    // Assim qualquer combinação funciona: nenhum, um, dois ou todos os filtros.
    @Query("""
            SELECT t FROM Transacao t
            WHERE (CAST(:tipo AS string) IS NULL OR UPPER(t.tipo) = UPPER(CAST(:tipo AS string)))
              AND (CAST(:categoria AS string) IS NULL OR UPPER(t.categoria) = UPPER(CAST(:categoria AS string)))
              AND (CAST(:descricao AS string) IS NULL
                   OR UPPER(t.descricao) LIKE UPPER(CONCAT('%', CAST(:descricao AS string), '%')))
              AND (CAST(:valorMin AS bigdecimal) IS NULL OR t.valor >= :valorMin)
              AND (CAST(:valorMax AS bigdecimal) IS NULL OR t.valor <= :valorMax)
            ORDER BY t.id
            """)
    List<Transacao> filtrar(@Param("tipo") String tipo,
                            @Param("categoria") String categoria,
                            @Param("descricao") String descricao,
                            @Param("valorMin") BigDecimal valorMin,
                            @Param("valorMax") BigDecimal valorMax);
}
