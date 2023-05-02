package br.com.ada.testeautomatizado.util;

import br.com.ada.testeautomatizado.exception.PlacaInvalidaException;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoPlaca {
    private static final String REGEX_MERCOSUL_BRASIL = "(^[A-Z]{3}-?\\d[0-9A-Z]\\d{2}$)";
    private static boolean noMatches(String placa) {
        boolean matchTodas = placa.matches(REGEX_MERCOSUL_BRASIL);
        return !(matchTodas);
    }
    
    public void isPlacaValida(String placa) {
        if(noMatches(placa)) throw new PlacaInvalidaException();
    }
    
}