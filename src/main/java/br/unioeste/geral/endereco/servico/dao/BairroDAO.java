package br.unioeste.geral.endereco.servico.dao;

import br.unioeste.apoio.bd.ConexaoBD;
import br.unioeste.geral.endereco.bo.bairro.Bairro;
import br.unioeste.geral.endereco.servico.exception.EnderecoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BairroDAO {
    private final ConexaoBD conexaoBD;

    public BairroDAO() {
        conexaoBD = new ConexaoBD();
    }

    public Bairro obterBairroPorID(Long id) throws Exception {
        String sql = "SELECT * FROM bairro WHERE id = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        Bairro bairro = null;

        try{
            conexao = conexaoBD.getConexaoBD();
            stmt = conexao.prepareStatement(sql);

            conexao.setAutoCommit(false);

            stmt.setLong(1, id);

            resultSet = stmt.executeQuery();

            if(resultSet.next()){
                bairro = criarBairroBO(resultSet);
            }

            conexao.commit();
        }
        catch(SQLException e){
            throw new EnderecoException("Não foi possível encontrar o bairro com a ID: " + id);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados");
        }
        finally {
            conexaoBD.encerrarConexoes(resultSet, stmt, conexao);
        }

        return bairro;
    }

    public List<Bairro> obterBairros() throws Exception{
        String sql = "SELECT * FROM bairro";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        List<Bairro> bairros = new ArrayList<>();

        try{
            conexao = conexaoBD.getConexaoBD();
            stmt = conexao.prepareStatement(sql);

            conexao.setAutoCommit(false);

            resultSet = stmt.executeQuery();

            while (resultSet.next()){
                bairros.add(criarBairroBO(resultSet));
            }

            conexao.commit();
        }
        catch(SQLException e){
            throw new EnderecoException("Não foi possível buscar todos os bairros");
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados");
        }

        return bairros;
    }

    private Bairro criarBairroBO(ResultSet resultSet) throws Exception {
        long id = resultSet.getLong("id");
        String nome = resultSet.getString("nome");

        return new Bairro(id, nome);
    }
}
