package br.com.ada.testeautomatizado.exception;

public class VeiculoNaoEncontradoException extends RuntimeException {

    public VeiculoNaoEncontradoException() {
        super(" : Placa do Veiculo nao encontrada na base de dados!");
    }

}
