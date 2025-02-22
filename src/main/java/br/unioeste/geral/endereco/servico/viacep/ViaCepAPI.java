package br.unioeste.geral.endereco.servico.viacep;

import br.unioeste.geral.endereco.bo.bairro.Bairro;
import br.unioeste.geral.endereco.bo.cidade.Cidade;
import br.unioeste.geral.endereco.bo.endereco.Endereco;
import br.unioeste.geral.endereco.bo.logradouro.Logradouro;
import br.unioeste.geral.endereco.bo.unidadefederativa.UnidadeFederativa;
import br.unioeste.geral.endereco.servico.exception.EnderecoException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ViaCepAPI {
    private static final String VIA_CEP_API_URL = "http://viacep.com.br/ws/";

    public Endereco obterEnderecoPorCEP(String cep) throws Exception {
        String enderecoJSON = obterEnderecoViaCepAPI(cep);
        Map<String, String> enderecoMap = converterJSONParaMap(enderecoJSON);

        if(enderecoMap.containsKey("erro")){
            throw new EnderecoException("Não existe endereco com CEP: " + cep);
        }

        return criarEndereco(enderecoMap);
    }


    private String obterEnderecoViaCepAPI(String cep) throws Exception {
        String urlCepServico = VIA_CEP_API_URL + cep + "/json";

        URL url = new URL(urlCepServico);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

        conexao.setRequestMethod("GET");

        if(conexao.getResponseCode() != HttpURLConnection.HTTP_OK){
            throw new RuntimeException("Não foi possível se conectar a API do ViaCEP");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()))) {
            StringBuilder conteudoBuilder = new StringBuilder();
            String conteudo;

            while ((conteudo = reader.readLine()) != null) {
                conteudoBuilder.append(conteudo);
            }

            return conteudoBuilder.toString();
        }
    }

    private Map<String, String> converterJSONParaMap(String json) {
        Map<String, String> map = new HashMap<>();

        json = json.replaceAll("[{}\"]", "");

        String[] pairs = json.split(",");

        for (String pair : pairs) {
            String[] entry = pair.split(":");
            map.put(entry[0].trim(), entry[1].trim());
        }

        return map;
    }

    private Endereco criarEndereco(Map<String, String> map) {
        String siglaUnidadeFederativa = map.getOrDefault("uf", "");
        String nomeUnidadeFederativa = map.getOrDefault("estado", "");

        String nomeCidade = map.getOrDefault("localidade", "");
        String nomeBairro = map.getOrDefault("bairro", "");

        String nomeLogradouro = map.getOrDefault("logradouro", "");

        String cep = map.getOrDefault("cep", "");

        UnidadeFederativa unidadeFederativa = new UnidadeFederativa(siglaUnidadeFederativa, nomeUnidadeFederativa);
        Cidade cidade = new Cidade(-1L, nomeCidade, unidadeFederativa);
        Bairro bairro = new Bairro(-1L, nomeBairro);

        Logradouro logradouro = new Logradouro(-1L, nomeLogradouro, null);

        return new Endereco(-1L, cep, bairro, logradouro, cidade);
    }
}
