package br.com.serasa.scoreapp.services;

import br.com.serasa.scoreapp.domain.Endereco;
import br.com.serasa.scoreapp.domain.Pessoa;
import br.com.serasa.scoreapp.dto.EnderecoViaCepResponseDto;
import br.com.serasa.scoreapp.dto.PessoaDto;
import br.com.serasa.scoreapp.exceptions.EnderecoException;
import br.com.serasa.scoreapp.repositories.PessoaRepository;
import br.com.serasa.scoreapp.utils.MaskUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private MaskUtils maskUtils;

    public Pessoa save(PessoaDto pessoaDto, EnderecoViaCepResponseDto enderecoViaCepResponseDto) throws EnderecoException{

        if(StringUtils.isBlank(enderecoViaCepResponseDto.getCep())) throw new EnderecoException("O endereço não é válido. Forneça um CEP válido");

        Endereco endereco = new Endereco();
        endereco.setCep(maskUtils.removerMascara(enderecoViaCepResponseDto.getCep()));
        endereco.setBairro(enderecoViaCepResponseDto.getBairro());
        endereco.setCidade(enderecoViaCepResponseDto.getLocalidade());
        endereco.setEstado(enderecoViaCepResponseDto.getEstado());
        endereco.setLogradouro(enderecoViaCepResponseDto.getLogradouro());

        Pessoa pessoa = new Pessoa();
        pessoa.setNome(pessoaDto.getNome());
        pessoa.setEndereco(endereco);
        pessoa.setIdade(pessoaDto.getIdade());
        pessoa.setScore(pessoaDto.getScore());
        pessoa.setTelefone(pessoaDto.getTelefone());

        return pessoaRepository.save(pessoa);
    }

    public Optional<Pessoa> findById(Long id){
        return pessoaRepository.findById(id);
    }

    public Page<Pessoa> findAllByFilters(String nome, Integer idade, String cep, Pageable pageable){
        return pessoaRepository.findByFilters(nome, idade, cep, pageable);
    }

    public void delete(Pessoa pessoa){
        pessoaRepository.delete(pessoa);
    }
}
