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
    private final ConexaoBD conexaoBD;
    private final UnidadeFederativaDAO unidadeFederativaDAO;

    public CidadeDAO() {
        conexaoBD = new ConexaoBD();
        unidadeFederativaDAO = new UnidadeFederativaDAO();
    }

    public Cidade obterCidadePorID(Long id) throws Exception {
        String sql = "SELECT * FROM cidade WHERE id = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        Cidade cidade = null;

        try{
            conexao = conexaoBD.getConexaoBD();
            stmt = conexao.prepareStatement(sql);

            conexao.setAutoCommit(false);

            stmt.setLong(1, id);

            resultSet = stmt.executeQuery();

            if(resultSet.next()){
                return criarCidadeBO(resultSet);
            }

            conexao.commit();
        }
        catch(SQLException e){
            throw new EnderecoException("Não foi possível encontrar a cidade com o ID: " + id);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados");
        }
        finally {
            conexaoBD.encerrarConexoes(resultSet, stmt, conexao);
        }

        return cidade;
    }

    public List<Cidade> obterCidades() throws Exception{
        String sql = "SELECT * FROM cidade";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        List<Cidade> cidades = new ArrayList<>();

        try{
            conexao = conexaoBD.getConexaoBD();
            stmt = conexao.prepareStatement(sql);

            conexao.setAutoCommit(false);

            resultSet = stmt.executeQuery();

            while (resultSet.next()){
                cidades.add(criarCidadeBO(resultSet));
            }

            conexao.commit();
        }
        catch(SQLException e){
            throw new EnderecoException("Não foi possível buscar todas as cidades");
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados");
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
