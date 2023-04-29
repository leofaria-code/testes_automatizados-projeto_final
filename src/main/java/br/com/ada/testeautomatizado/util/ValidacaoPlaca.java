package br.com.ada.testeautomatizado.util;

import br.com.ada.testeautomatizado.exception.PlacaInvalidaException;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoPlaca {
    public static final String REGEX_BRASIL_ANTIGA = "(^[A-Z]{3}-?\\d{4}$)";
    public static final String REGEX_MERCOSUL_ATUAL = "(^[A-Z]{3}(\\d)(A-Z)(\\d{2})$)";
    
    public void isPlacaValida(String placa) {
        if(noMatches(placa)) throw new PlacaInvalidaException();
    }
    
    private static boolean noMatches(String placa) {
        boolean matchMercosul = placa.matches(REGEX_MERCOSUL_ATUAL);
        boolean matchBrasil = placa.matches(REGEX_BRASIL_ANTIGA);
        return !(matchMercosul||matchBrasil);
    }
    
}