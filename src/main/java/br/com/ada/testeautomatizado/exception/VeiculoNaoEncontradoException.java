package br.com.ada.testeautomatizado.exception;

public class VeiculoNaoEncontradoException extends RuntimeException {

    public VeiculoNaoEncontradoException() {
        super("Veículo não encontrado");
    }

}
