package br.com.serasa.scoreapp.repositories;

import br.com.serasa.scoreapp.domain.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    @Query("SELECT p FROM Pessoa p WHERE" +
            " (:idade is null or p.idade=:idade) AND" +
            " (:cep is null or p.endereco.cep=:cep) AND" +
            " (:nome is null or LOWER(p.nome) like LOWER(concat(:nome, '%')))")
    public Page<Pessoa> findByFilters(
            @Param("nome") String nome,
            @Param("idade") Integer idade,
            @Param("cep") String cep,
            Pageable pageable);
}
