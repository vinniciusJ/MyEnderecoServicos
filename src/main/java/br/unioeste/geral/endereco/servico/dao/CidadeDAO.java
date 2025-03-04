package br.unioeste.geral.endereco.servico.dao;

import br.unioeste.apoio.bd.ConexaoBD;
import br.unioeste.geral.endereco.bo.bairro.Bairro;
import br.unioeste.geral.endereco.bo.cidade.Cidade;
import br.unioeste.geral.endereco.bo.unidadefederativa.UnidadeFederativa;
import br.unioeste.geral.endereco.servico.exception.EnderecoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CidadeDAO {
    private final Connection conexao;
    private final UnidadeFederativaDAO unidadeFederativaDAO;

    public CidadeDAO(Connection conexao) {
        this.conexao = conexao;
        this.unidadeFederativaDAO = new UnidadeFederativaDAO(conexao);
    }

    public Cidade obterCidadePorID(Long id) throws Exception {
        String sql = "SELECT * FROM cidade WHERE id = ?";

        Cidade cidade = null;

        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setLong(1, id);

            try(ResultSet resultSet = stmt.executeQuery()){
                if (resultSet.next()){
                    cidade = criarCidadeBO(resultSet);
                }
            }
        }
        catch (Exception e){
            throw new EnderecoException("Não foi possível obter a cidade com ID " + id);
        }

        return cidade;
    }

    public List<Cidade> obterCidades() throws Exception{
        String sql = "SELECT * FROM cidade";

        List<Cidade> cidades = new ArrayList<>();

        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            try(ResultSet resultSet = stmt.executeQuery()){
                while (resultSet.next()){
                    cidades.add(criarCidadeBO(resultSet));
                }
            }
        }
        catch (Exception e){
            throw new EnderecoException("Não foi possível obter todos as cidades");
        }

        return cidades;
    }

    private Cidade criarCidadeBO(ResultSet resultSet) throws Exception {
        long id = resultSet.getLong("id");
        String nomeCidade = resultSet.getString("nome");
        String siglaUnidadeFederativa = resultSet.getString("sigla_unidade_federativa");

        UnidadeFederativa unidadeFederativa = unidadeFederativaDAO.obterUnidadeFederativaPelaSigla(siglaUnidadeFederativa);

        return new Cidade(id, nomeCidade, unidadeFederativa);
    }
}
