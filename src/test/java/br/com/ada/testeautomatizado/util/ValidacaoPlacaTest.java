package br.com.ada.testeautomatizado.util;

import br.com.ada.testeautomatizado.exception.PlacaInvalidaException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidacaoPlacaTest {

    @Spy
    private ValidacaoPlaca validacaoPlaca;

    @Test
    void deveriaRetornarPlacaValida() {
        String placa = "XYZ-4578";
        validacaoPlaca.isPlacaValida(placa);
        Mockito.verify(validacaoPlaca).isPlacaValida(placa);
    }

    @Test
    void deveriaRetornarPlacaInvalida() {
        String placa = "XYZ-578";
        Assertions.assertThrows(PlacaInvalidaException.class, () -> validacaoPlaca.isPlacaValida(placa));
    }
}