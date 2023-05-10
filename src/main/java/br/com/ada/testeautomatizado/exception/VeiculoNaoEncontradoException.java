package br.com.ada.testeautomatizado.exception;

public class VeiculoNaoEncontradoException extends RuntimeException {

    public VeiculoNaoEncontradoException() {
        super(" : Placa do Veículo não encontrada na base de dados!");
    }

}
