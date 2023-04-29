package br.com.ada.testeautomatizado.exception;

public class PlacaInvalidaException extends RuntimeException {
    public PlacaInvalidaException(){
        super("Placa inválida para Brasil ou Mercosul!");
    }
    
}