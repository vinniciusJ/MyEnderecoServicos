package br.unioeste.geral.endereco.servico.dao;

import br.unioeste.apoio.bd.ConexaoBD;
import br.unioeste.geral.endereco.bo.logradouro.Logradouro;
import br.unioeste.geral.endereco.bo.tipologradouro.TipoLogradouro;
import br.unioeste.geral.endereco.servico.exception.EnderecoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LogradouroDAO {
    private final TipoLogradouroDAO tipoLogradouroDAO;
    private final ConexaoBD conexaoBD;

    public LogradouroDAO() {
        tipoLogradouroDAO = new TipoLogradouroDAO();
        conexaoBD = new ConexaoBD();
    }

    public Logradouro obterLogradouroPorId(Long id) throws Exception {
        String sql = "SELECT * FROM logradouro WHERE id = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        Logradouro logradouro = new Logradouro();

        try{
            conexao = conexaoBD.getConexaoBD();
            stmt = conexao.prepareStatement(sql);

            conexao.setAutoCommit(false);

            stmt.setLong(1, id);

            resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                logradouro = criarLogradouroBO(resultSet);
            }

            conexao.commit();;
        }
        catch(SQLException e){
            throw new EnderecoException("Não foi possível encontrar o logradouro com a ID: " + id);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados");
        }
        finally {
            conexaoBD.encerrarConexoes(resultSet, stmt, conexao);
        }

        return logradouro;
    }

    public List<Logradouro> obterLogradouros() throws Exception {
        String sql = "SELECT * FROM logradouro";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        List<Logradouro> logradouros = new ArrayList<Logradouro>();

        try{
            conexao = conexaoBD.getConexaoBD();
            stmt = conexao.prepareStatement(sql);

            conexao.setAutoCommit(false);

            resultSet = stmt.executeQuery();

            while(resultSet.next()) {
                logradouros.add(criarLogradouroBO(resultSet));
            }

            conexao.commit();
        }
        catch(SQLException e){
            throw new EnderecoException("Não foi possível buscar todos os logradouros");
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados");
        }

        return logradouros;
    }

    private Logradouro criarLogradouroBO(ResultSet resultSet) throws Exception {
        long id = resultSet.getLong("id");
        String nomeLogradouro = resultSet.getString("nome");
        String siglaTipoLogradouro = resultSet.getString("sigla_tipo_logradouro");

        TipoLogradouro tipoLogradouro = tipoLogradouroDAO.obterTipoLogradouroPelaSigla(siglaTipoLogradouro);

        return new Logradouro(id, nomeLogradouro, tipoLogradouro);
    }

}
