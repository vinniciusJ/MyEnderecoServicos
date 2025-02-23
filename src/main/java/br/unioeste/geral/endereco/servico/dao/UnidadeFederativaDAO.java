package br.unioeste.geral.endereco.servico.dao;

import br.unioeste.apoio.bd.ConexaoBD;
import br.unioeste.geral.endereco.bo.bairro.Bairro;
import br.unioeste.geral.endereco.bo.unidadefederativa.UnidadeFederativa;
import br.unioeste.geral.endereco.servico.exception.EnderecoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UnidadeFederativaDAO {
    private final ConexaoBD conexaoBD;

    public UnidadeFederativaDAO() {
        conexaoBD = new ConexaoBD();
    }

    public UnidadeFederativa obterUnidadeFederativaPelaSigla(String sigla) throws Exception {
        String sql = "SELECT * FROM unidade_federativa WHERE sigla = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        UnidadeFederativa unidadeFederativa = null;

        try{
            conexao = conexaoBD.getConexaoBD();
            stmt = conexao.prepareStatement(sql);

            conexao.setAutoCommit(false);

            stmt.setString(1, sigla);

            resultSet = stmt.executeQuery();

            if(resultSet.next()){
                unidadeFederativa = criarUnidadeFederativaBO(resultSet);
            }

            conexao.commit();
        }
        catch(SQLException e){
            throw new EnderecoException("Não foi possível encontrar o unidade federativa com a sigla: " + sigla);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados");
        }
        finally {
            conexaoBD.encerrarConexoes(resultSet, stmt, conexao);
        }

        return unidadeFederativa;
    }

    public List<UnidadeFederativa> obterUnidadesFederativas() throws Exception{
        String sql = "SELECT * FROM unidade_federativa";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        List<UnidadeFederativa> unidadeFederativas = new ArrayList<>();

        try{
            conexao = conexaoBD.getConexaoBD();
            stmt = conexao.prepareStatement(sql);

            conexao.setAutoCommit(false);

            resultSet = stmt.executeQuery();

            while (resultSet.next()){
                unidadeFederativas.add(criarUnidadeFederativaBO(resultSet));
            }

            conexao.commit();
        }
        catch(SQLException e){
            throw new EnderecoException("Não foi possível buscar todas as unidades federativas");
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados");
        }
        finally {
            conexaoBD.encerrarConexoes(resultSet, stmt, conexao);
        }

        return unidadeFederativas;
    }

    private UnidadeFederativa criarUnidadeFederativaBO(ResultSet resultSet) throws Exception {
        String sigla = resultSet.getString("sigla");
        String nome = resultSet.getString("nome");

        return new UnidadeFederativa(sigla, nome);
    }
}
