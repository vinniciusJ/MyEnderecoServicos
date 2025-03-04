package br.unioeste.geral.endereco.servico.dao;

import br.unioeste.apoio.bd.ConexaoBD;
import br.unioeste.geral.endereco.bo.logradouro.Logradouro;
import br.unioeste.geral.endereco.bo.tipologradouro.TipoLogradouro;
import br.unioeste.geral.endereco.servico.exception.EnderecoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LogradouroDAO {
    private final TipoLogradouroDAO tipoLogradouroDAO;

    private final Connection conexao;

    public LogradouroDAO(Connection conexao) {
        this.conexao = conexao;
        this.tipoLogradouroDAO = new TipoLogradouroDAO(conexao);
    }

    public Logradouro obterLogradouroPorId(Long id) throws Exception {
        String sql = "SELECT * FROM logradouro WHERE id = ?";

        Logradouro logradouro = null;

        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            stmt.setLong(1, id);

            try(ResultSet resultSet = stmt.executeQuery()){
                if (resultSet.next()){
                    logradouro = criarLogradouroBO(resultSet);
                }
            }
        }
        catch (Exception e){
            throw new EnderecoException("Não foi possível obter o logradouro com ID " + id);
        }

        return logradouro;
    }

    public List<Logradouro> obterLogradouros() throws Exception {
        String sql = "SELECT * FROM logradouro";

        List<Logradouro> logradouros = new ArrayList<Logradouro>();

        try(PreparedStatement stmt = conexao.prepareStatement(sql)){
            try(ResultSet resultSet = stmt.executeQuery()){
                while (resultSet.next()){
                    logradouros.add(criarLogradouroBO(resultSet));
                }
            }
        }
        catch (Exception e){
            throw new EnderecoException("Não foi possível obter todos os logradouros");
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
