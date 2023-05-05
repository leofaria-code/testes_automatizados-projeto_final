package br.com.ada.testeautomatizado.exception;

public class PlacaInvalidaException extends RuntimeException {
    public PlacaInvalidaException(){
        super(" : Placa invalida para Brasil ou Mercosul!");
    }
    
}