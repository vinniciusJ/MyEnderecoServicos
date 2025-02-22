package br.unioeste.geral.endereco.servico.service;

import br.unioeste.geral.endereco.bo.bairro.Bairro;
import br.unioeste.geral.endereco.bo.cidade.Cidade;
import br.unioeste.geral.endereco.bo.endereco.Endereco;
import br.unioeste.geral.endereco.bo.logradouro.Logradouro;
import br.unioeste.geral.endereco.servico.col.CidadeCOL;
import br.unioeste.geral.endereco.servico.col.EnderecoCOL;
import br.unioeste.geral.endereco.servico.dao.BairroDAO;
import br.unioeste.geral.endereco.servico.dao.CidadeDAO;
import br.unioeste.geral.endereco.servico.dao.EnderecoDAO;
import br.unioeste.geral.endereco.servico.dao.LogradouroDAO;
import br.unioeste.geral.endereco.servico.exception.EnderecoException;
import br.unioeste.geral.endereco.servico.viacep.ViaCepAPI;

import java.util.List;

public class UCEnderecoServicos {
    private final EnderecoCOL enderecoCOL;
    private final CidadeCOL cidadeCOL;

    private final EnderecoDAO enderecoDAO;
    private final CidadeDAO cidadeDAO;
    private final LogradouroDAO logradouroDAO;
    private final BairroDAO bairroDAO;

    private final ViaCepAPI viaCepAPI;

    public UCEnderecoServicos(){
        enderecoCOL = new EnderecoCOL();
        cidadeCOL = new CidadeCOL();

        enderecoDAO = new EnderecoDAO();
        cidadeDAO = new CidadeDAO();
        logradouroDAO = new LogradouroDAO();
        bairroDAO = new BairroDAO();

        viaCepAPI = new ViaCepAPI();
    }

    public List<Endereco> obterEnderecos() throws Exception{
        return enderecoDAO.obterEnderecos();
    }

    public List<Endereco> obterEnderecosPorCEP(String cep) throws Exception{
        if(!enderecoCOL.validarCEP(cep)){
            throw new EnderecoException("CEP inválido: " + cep);
        }

        return enderecoDAO.obterEnderecosPorCEP(cep);
    }

    public Endereco obterEnderecoPorID(Long id) throws Exception{
        if(!enderecoCOL.validarID(id)){
            throw new EnderecoException("ID inválido: " + id);
        }

        Endereco endereco = enderecoDAO.obterEnderecoPorID(id);

        if(endereco == null){
            throw new EnderecoException("Endereço não encontrado com ID: " + id);
        }

        return endereco;
    }

    public Endereco obterEnderecoExternoPorCEP(String cep) throws Exception {
        if(!enderecoCOL.validarCEP(cep)){
            throw new EnderecoException("CEP inválido: " + cep);
        }

        Endereco endereco = viaCepAPI.obterEnderecoPorCEP(cep);

        if(endereco == null){
            throw new EnderecoException("Não há endereço com CEP: " + cep);
        }

        return endereco;
    }

    public Endereco cadastrarEndereco(Endereco endereco) throws Exception{
        if(!enderecoCOL.validarEndereco(endereco)){
            throw new EnderecoException("Endereço inválido");
        }

        return enderecoDAO.inserirEndereco(endereco);
    }

    public Cidade obterCidadePorID(Long id) throws Exception{
        if(!cidadeCOL.validarID(id)){
            throw new EnderecoException("ID inválido: " + id);
        }

        Cidade cidade = cidadeDAO.obterCidadePorID(id);

        if(cidade == null){
            throw new EnderecoException("Cidade não encontrada com ID: " + id);
        }

        return cidade;
    }

    public List<Cidade> obterCidades() throws Exception{
        return cidadeDAO.obterCidades();
    }

    public List<Logradouro> obterLogradouros() throws Exception {
        return logradouroDAO.obterLogradouros();
    }

    public List<Bairro> obterBairros() throws Exception {
        return bairroDAO.obterBairros();
    }
}
