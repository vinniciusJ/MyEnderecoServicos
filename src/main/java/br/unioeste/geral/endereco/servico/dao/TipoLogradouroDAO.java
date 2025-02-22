package br.unioeste.geral.endereco.servico.dao;

import br.unioeste.apoio.bd.ConexaoBD;
import br.unioeste.geral.endereco.bo.tipologradouro.TipoLogradouro;
import br.unioeste.geral.endereco.servico.exception.EnderecoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoLogradouroDAO {
    private final ConexaoBD conexaoBD;

    public TipoLogradouroDAO() {
        conexaoBD = new ConexaoBD();
    }

    public TipoLogradouro obterTipoLogradouroPelaSigla(String sigla) throws Exception {
        String sql = "SELECT * FROM tipo_logradouro WHERE sigla = ?";

        try{
            Connection conexao = conexaoBD.getConexaoBD();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            conexao.setAutoCommit(false);

            stmt.setString(1, sigla);

            try(ResultSet resultSet = stmt.executeQuery()){
                if(resultSet.next()){
                    return criarTipoLogradouroBO(resultSet);
                }
            }

            conexao.commit();
        }
        catch(SQLException e){
            throw new EnderecoException("Não foi possível encontrar o tipo de logradouro com a sigla: " + sigla);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados");
        }

        return null;
    }

    public List<TipoLogradouro> obterTipoLogradouros() throws Exception{
        String sql = "SELECT * FROM tipo_logradouro";

        List<TipoLogradouro> tipoLogradouros = new ArrayList<TipoLogradouro>();

        try{
            Connection conexao = conexaoBD.getConexaoBD();
            PreparedStatement stmt = conexao.prepareStatement(sql);

            conexao.setAutoCommit(false);

            try( ResultSet resultSet = stmt.executeQuery()){
                while (resultSet.next()){
                    tipoLogradouros.add(criarTipoLogradouroBO(resultSet));
                }
            }

            conexao.commit();
        }
        catch(SQLException e){
            throw new EnderecoException("Não foi possível buscar todos os tipos de logradouro");
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados");
        }

        return tipoLogradouros;
    }

    private TipoLogradouro criarTipoLogradouroBO(ResultSet resultSet) throws Exception {
        String nomeTipoLogradouro = resultSet.getString("nome");
        String siglaTipoLogradouro = resultSet.getString("sigla");

        return new TipoLogradouro(siglaTipoLogradouro, nomeTipoLogradouro);
    }
}
