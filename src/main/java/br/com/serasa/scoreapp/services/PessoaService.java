package br.com.serasa.scoreapp.services;

import br.com.serasa.scoreapp.domain.Endereco;
import br.com.serasa.scoreapp.domain.Pessoa;
import br.com.serasa.scoreapp.dto.EnderecoViaCepResponseDto;
import br.com.serasa.scoreapp.dto.PessoaDto;
import br.com.serasa.scoreapp.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public Pessoa save(PessoaDto pessoaDto, EnderecoViaCepResponseDto enderecoViaCepResponseDto){

        Endereco endereco = new Endereco();
        endereco.setCep(enderecoViaCepResponseDto.getCep());
        endereco.setBairro(enderecoViaCepResponseDto.getBairro());
        endereco.setCidade(enderecoViaCepResponseDto.getLocalidade());
        endereco.setEstado(enderecoViaCepResponseDto.getEstado());
        endereco.setLogradouro(enderecoViaCepResponseDto.getLogradouro());

        Pessoa pessoa = new Pessoa();
        pessoa.setNome(pessoa.getNome());
        pessoa.setEndereco(endereco);
        pessoa.setIdade(pessoaDto.getIdade());
        pessoa.setScore(pessoa.getScore());
        pessoa.setTelefone(pessoaDto.getTelefone());

        return pessoaRepository.save(pessoa);
    }

    public Optional<Pessoa> findById(Long id){
        return pessoaRepository.findById(id);
    }
}
