package br.com.ada.testeautomatizado.util;

import br.com.ada.testeautomatizado.exception.PlacaInvalidaException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidacaoPlacaTest {

    @Spy    // placas antigas e placas mercosul novas
    private ValidacaoPlaca validacaoPlaca;
    
    @ParameterizedTest (name = "Placa {index}: {arguments}")
    @ValueSource(strings = {"ABC-1234", "ABC1234", "ABC1C34", "ABC-1C34"})
    @DisplayName("Validar PLACA sucesso")
    void deveriaRetornarPlacaValida(String placa) {
        validacaoPlaca.isPlacaValida(placa);
        Mockito.verify(validacaoPlaca).isPlacaValida(placa);
    }
    
    @ParameterizedTest (name = "Placa {index}: {arguments}")
    @ValueSource(strings = {"ABC-DEfG", "abc1234", "1231C34", "ABC-1CE4"})
    @DisplayName("Validar PLACA erro")
    void deveriaRetornarPlacaInvalida(String placa) {
        Assertions.assertThrows(PlacaInvalidaException.class, () -> validacaoPlaca.isPlacaValida(placa));
    }
}