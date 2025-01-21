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

    public Page<Pessoa> findByNome(String nome, Pageable pageable);
    public Page<Pessoa> findByIdade(int idade, Pageable pageable);

    @Query("SELECT p,e FROM Pessoa p JOIN p.endereco e WHERE p.endereco.cep=:cep")
    public Page<Pessoa> findByCep(@Param("cep") String cep, Pageable pageable);
}
