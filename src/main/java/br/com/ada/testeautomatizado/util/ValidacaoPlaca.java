package br.com.ada.testeautomatizado.util;

import br.com.ada.testeautomatizado.exception.PlacaInvalidaException;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoPlaca {

    public void isPlacaValida(String placa) {
        if(noMatches(placa))
            throw new PlacaInvalidaException();
    }

    private static boolean noMatches(String placa) {
        return !placa.matches("([A-Z]{3})(\\-)(\\d{4})");
    }

}
