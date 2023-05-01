package br.com.ada.testeautomatizado.exception;

import lombok.Builder;

@Builder
public class PlacaInvalidaException extends RuntimeException {
    public PlacaInvalidaException(){
        super(" : Placa invalida para Brasil ou Mercosul!");
    }
    
}