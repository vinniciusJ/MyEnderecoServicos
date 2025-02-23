package br.unioeste.geral.endereco.servico.dao;

import br.unioeste.apoio.bd.ConexaoBD;
import br.unioeste.geral.endereco.bo.bairro.Bairro;
import br.unioeste.geral.endereco.bo.cidade.Cidade;
import br.unioeste.geral.endereco.bo.endereco.Endereco;
import br.unioeste.geral.endereco.bo.logradouro.Logradouro;
import br.unioeste.geral.endereco.servico.exception.EnderecoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO {
    private final ConexaoBD conexaoBD;
    private final BairroDAO bairroDAO;
    private final CidadeDAO cidadeDAO;
    private final LogradouroDAO logradouroDAO;

    public EnderecoDAO() {
        conexaoBD = new ConexaoBD();

        bairroDAO = new BairroDAO();
        cidadeDAO = new CidadeDAO();
        logradouroDAO = new LogradouroDAO();
    }

    public Endereco obterEnderecoPorID(Long id) throws Exception {
        String sql = "SELECT * FROM endereco WHERE id = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        Endereco endereco = null;

        try{
            conexao = conexaoBD.getConexaoBD();
            stmt = conexao.prepareStatement(sql);

            conexao.setAutoCommit(false);

            stmt.setLong(1, id);

            resultSet = stmt.executeQuery();

            if(resultSet.next()){
                endereco = criarEnderecoBO(resultSet);
            }

            conexao.commit();
        }
        catch(SQLException e){
            throw new EnderecoException("Não foi possível encontrar o endereço com a ID: " + id);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados");
        }
        finally {
            conexaoBD.encerrarConexoes(resultSet, stmt, conexao);
        }

        return endereco;
    }


    public List<Endereco> obterEnderecos() throws Exception{
        String sql = "SELECT * FROM endereco";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        List<Endereco> enderecos = new ArrayList<>();

        try{
            conexao = conexaoBD.getConexaoBD();
            stmt = conexao.prepareStatement(sql);

            conexao.setAutoCommit(false);

            resultSet = stmt.executeQuery();

            while (resultSet.next()){
                enderecos.add(criarEnderecoBO(resultSet));
            }

            conexao.commit();
        }
        catch(SQLException e){
            throw new EnderecoException("Não foi possível buscar todos os endereços ");
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados");
        }
        finally {
            conexaoBD.encerrarConexoes(resultSet, stmt, conexao);
        }

        return enderecos;
    }

    public List<Endereco> obterEnderecosPorCEP(String cep) throws Exception{
        String sql = "SELECT * FROM endereco WHERE cep = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        List<Endereco> enderecos = new ArrayList<>();

        try{
            conexao = conexaoBD.getConexaoBD();
            stmt = conexao.prepareStatement(sql);

            conexao.setAutoCommit(false);

            stmt.setString(1, cep);

            resultSet = stmt.executeQuery();

            while (resultSet.next()){
                enderecos.add(criarEnderecoBO(resultSet));
            }

            conexao.commit();
        }
        catch(SQLException e){
            throw new EnderecoException("Não foi possível buscar todos os endereços ");
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados");
        }
        finally {
            conexaoBD.encerrarConexoes(resultSet, stmt, conexao);
        }

        return enderecos;
    }

    public Endereco inserirEndereco(Endereco endereco) throws Exception {
        String sql = "INSERT INTO endereco (cep, id_logradouro, id_bairro, id_cidade) VALUES (?, ?, ?, ?)";

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try{
            conexao = conexaoBD.getConexaoBD();
            stmt = conexao.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            conexao.setAutoCommit(false);

            stmt.setString(1, endereco.getCep());
            stmt.setLong(2, endereco.getLogradouro().getId());
            stmt.setLong(3, endereco.getBairro().getId());
            stmt.setLong(4, endereco.getCidade().getId());

            stmt.executeUpdate();

            resultSet = stmt.getGeneratedKeys();

            if(resultSet.next()){
                endereco.setId(resultSet.getLong(1));
            }
            else {
                throw new EnderecoException("Não foi possível cadastrar o endereço");
            }

            conexao.commit();
        }
        catch(SQLException e){
            if(conexao != null){
                conexao.rollback();
            }

            throw new EnderecoException("Não foi possível buscar todos os endereços ");
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível estabelecer conexão com o banco de dados");
        }
        finally {
            conexaoBD.encerrarConexoes(resultSet, stmt, conexao);
        }

        return endereco;
    }

    private Endereco criarEnderecoBO(ResultSet resultSet) throws Exception {
        long id = resultSet.getLong("id");
        String cep = resultSet.getString("cep");

        long idBairro = resultSet.getLong("bairro");
        long idLogradouro = resultSet.getLong("id_logradouro");
        long idCidade = resultSet.getLong("id_cidade");

        Bairro bairro = bairroDAO.obterBairroPorID(idBairro);
        Logradouro logradouro = logradouroDAO.obterLogradouroPorId(idLogradouro);
        Cidade cidade = cidadeDAO.obterCidadePorID(idCidade);

        return new Endereco(id, cep, bairro, logradouro, cidade);
    }
}
