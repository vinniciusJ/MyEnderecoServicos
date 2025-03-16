package br.unioeste.geral.endereco.servico.service;

import br.unioeste.apoio.bd.ConexaoBD;
import br.unioeste.geral.endereco.bo.bairro.Bairro;
import br.unioeste.geral.endereco.bo.cidade.Cidade;
import br.unioeste.geral.endereco.bo.endereco.Endereco;
import br.unioeste.geral.endereco.bo.logradouro.Logradouro;
import br.unioeste.geral.endereco.bo.tipologradouro.TipoLogradouro;
import br.unioeste.geral.endereco.bo.unidadefederativa.UnidadeFederativa;
import br.unioeste.geral.endereco.servico.col.CidadeCOL;
import br.unioeste.geral.endereco.servico.col.EnderecoCOL;
import br.unioeste.geral.endereco.servico.dao.*;
import br.unioeste.geral.endereco.servico.exception.EnderecoException;
import br.unioeste.geral.endereco.servico.viacep.ViaCepAPI;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class UCEnderecoServicos {
    private final EnderecoCOL enderecoCOL;
    private final CidadeCOL cidadeCOL;

    private final ConexaoBD conexaoBD;

    private final ViaCepAPI viaCepAPI;

    public UCEnderecoServicos(){
        enderecoCOL = new EnderecoCOL();
        cidadeCOL = new CidadeCOL();

        conexaoBD = new ConexaoBD();

        viaCepAPI = new ViaCepAPI();
    }

    public List<Endereco> obterEnderecos() throws Exception{
        List<Endereco> enderecos = new ArrayList<>();

        try(Connection conexao = conexaoBD.getConexaoBD()){
            EnderecoDAO enderecoDAO = new EnderecoDAO(conexao);

            conexao.setAutoCommit(false);

            try {
                enderecos = enderecoDAO.obterEnderecos();
                conexao.commit();
            }
            catch(Exception e){
                gerenciarException(e);
            }
        }

        return enderecos;
    }

    public List<Endereco> obterEnderecosPorCEP(String cep) throws Exception{
        if(!enderecoCOL.validarCEP(cep)){
            throw new EnderecoException("CEP inválido: " + cep);
        }

        List<Endereco> enderecos = new ArrayList<>();

        try(Connection conexao = conexaoBD.getConexaoBD()){
            EnderecoDAO enderecoDAO = new EnderecoDAO(conexao);

            conexao.setAutoCommit(false);

            try {
                enderecos = enderecoDAO.obterEnderecosPorCEP(cep);
                conexao.commit();
            }
            catch(Exception e){
                gerenciarException(e);
            }
        }

        return enderecos;
    }

    public Endereco obterEnderecoPorID(Long id) throws Exception{
        if(!enderecoCOL.validarID(id)){
            throw new EnderecoException("ID inválido: " + id);
        }

        Endereco endereco = null;

        try(Connection conexao = conexaoBD.getConexaoBD()){
            EnderecoDAO enderecoDAO = new EnderecoDAO(conexao);

            conexao.setAutoCommit(false);

            try{
                endereco = enderecoDAO.obterEnderecoPorID(id);

                conexao.commit();

                if(endereco == null){
                    throw new EnderecoException("Não foi possível encontrar endereço com ID " + id);
                }
            }
            catch(Exception exception){
                gerenciarException(exception);
            }
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
        if(!enderecoCOL.validarEnderecoForm(endereco)){
            throw new EnderecoException("Endereço inválido");
        }

        try(Connection conexao = conexaoBD.getConexaoBD()){
            EnderecoDAO enderecoDAO = new EnderecoDAO(conexao);

            conexao.setAutoCommit(false);

            try{
                Long idEndereco = enderecoDAO.inserirEndereco(endereco);

                endereco.setId(idEndereco);

                conexao.commit();
            }
            catch(Exception exception){
                conexao.rollback();

                gerenciarException(exception);
            }
        }

        return endereco;
    }

    public Cidade obterCidadePorID(Long id) throws Exception{
        if(!cidadeCOL.validarID(id)){
            throw new EnderecoException("ID inválido: " + id);
        }

        Cidade cidade = null;

        try(Connection conexao = conexaoBD.getConexaoBD()){
            CidadeDAO cidadeDAO = new CidadeDAO(conexao);

            conexao.setAutoCommit(false);

            try{
                cidade = cidadeDAO.obterCidadePorID(id);

                conexao.commit();

                if(cidade == null){
                    throw new EnderecoException("Não foi possível encontrar cidade com ID " + id);
                }
            }
            catch(Exception exception){
                gerenciarException(exception);
            }
        }

        return cidade;
    }

    public List<Cidade> obterCidades() throws Exception{
        List<Cidade> cidades = new ArrayList<>();

        try(Connection conexao = conexaoBD.getConexaoBD()){
            CidadeDAO cidadeDAO = new CidadeDAO(conexao);

            conexao.setAutoCommit(false);

            try {
                cidades = cidadeDAO.obterCidades();
                conexao.commit();
            }
            catch(Exception e){
                gerenciarException(e);
            }
        }

        return cidades;
    }

    public List<Cidade> obterCidadesPorNome(String nome) throws Exception{
        List<Cidade> cidades = new ArrayList<>();

        try(Connection conexao = conexaoBD.getConexaoBD()){
            CidadeDAO cidadeDAO = new CidadeDAO(conexao);

            conexao.setAutoCommit(false);

            try {
                cidades = cidadeDAO.obterCidadesPorNome(nome);
                conexao.commit();
            }
            catch(Exception e){
                gerenciarException(e);
            }
        }

        return cidades;
    }

    public List<Logradouro> obterLogradouros() throws Exception {
        List<Logradouro> logradouros = new ArrayList<>();

        try(Connection conexao = conexaoBD.getConexaoBD()){
            LogradouroDAO logradouroDAO = new LogradouroDAO(conexao);

            conexao.setAutoCommit(false);

            try {
                logradouros = logradouroDAO.obterLogradouros();
                conexao.commit();
            }
            catch(Exception e){
                gerenciarException(e);
            }
        }

        return logradouros;
    }

    public List<Bairro> obterBairros() throws Exception {
        List<Bairro> bairros = new ArrayList<>();

        try(Connection conexao = conexaoBD.getConexaoBD()){
            BairroDAO bairroDAO = new BairroDAO(conexao);

            conexao.setAutoCommit(false);

            try {
                bairros = bairroDAO.obterBairros();
                conexao.commit();
            }
            catch(Exception e){
                gerenciarException(e);
            }
        }

        return bairros;
    }

    public List<UnidadeFederativa> obterUnidadeFederativas() throws Exception {
        List<UnidadeFederativa> unidadeFederativas = new ArrayList<>();

        try(Connection conexao = conexaoBD.getConexaoBD()){
            UnidadeFederativaDAO unidadeFederativaDAO = new UnidadeFederativaDAO(conexao);

            conexao.setAutoCommit(false);

            try {
                unidadeFederativas = unidadeFederativaDAO.obterUnidadesFederativas();
                conexao.commit();
            }
            catch(Exception e){
                gerenciarException(e);
            }
        }

        return unidadeFederativas;
    }

    public List<TipoLogradouro> obterTiposLogradouros() throws Exception {
        List<TipoLogradouro> tipoLogradouros = new ArrayList<>();

        try(Connection conexao = conexaoBD.getConexaoBD()){
            TipoLogradouroDAO tipoLogradouroDAO = new TipoLogradouroDAO(conexao);

            conexao.setAutoCommit(false);

            try {
                tipoLogradouros = tipoLogradouroDAO.obterTipoLogradouros();
                conexao.commit();
            }
            catch(Exception e){
                gerenciarException(e);
            }
        }

        return tipoLogradouros;
    }

    private void gerenciarException(Exception exception) throws Exception{
        if(exception instanceof EnderecoException){
            throw exception;
        }

        throw new EnderecoException("Ocorreu um erro interno: " + exception.getMessage());
    }
}
