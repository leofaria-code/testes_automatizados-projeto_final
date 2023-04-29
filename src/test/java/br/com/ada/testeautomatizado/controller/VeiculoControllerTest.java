package br.com.ada.testeautomatizado.controller;


import br.com.ada.testeautomatizado.dto.VeiculoDTO;
import br.com.ada.testeautomatizado.model.Veiculo;
import br.com.ada.testeautomatizado.repository.VeiculoRepository;
import br.com.ada.testeautomatizado.service.VeiculoService;
import br.com.ada.testeautomatizado.util.Response;
import br.com.ada.testeautomatizado.util.ValidacaoPlaca;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class VeiculoControllerTest {

    @SpyBean
    private VeiculoService veiculoService;

    @MockBean
    private ValidacaoPlaca validacaoPlaca;

    @MockBean
    private VeiculoRepository veiculoRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Cadastrar veículo com sucesso")
    void cadastrarSucesso() throws Exception {

        String clienteString = mapper.writeValueAsString(veiculoDTO());

        Mockito.doCallRealMethod().when(validacaoPlaca).isPlacaValida(Mockito.anyString());

        MvcResult mvcResult = mockMvc.perform(post("/veiculo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clienteString)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String resultActual = mvcResult.getResponse().getContentAsString();
        Response<VeiculoDTO> response = Response.<VeiculoDTO>builder().message("Sucesso").detail(veiculoDTO()).build();
        String responseString = mapper.writeValueAsString(response);

        Assertions.assertEquals(responseString, resultActual);

    }


    @Test
    @DisplayName("Erro ao cadastrar veículo com placa inválida")
    void deveriaRetornarErroCadastrarVeiculoPlacaInvalida() throws Exception {

        VeiculoDTO veiculoDTO = veiculoDTO();
        veiculoDTO.setPlaca("QWL1231");
        String clienteString = mapper.writeValueAsString(veiculoDTO);

        Mockito.doCallRealMethod().when(validacaoPlaca).isPlacaValida(Mockito.anyString());

        MvcResult mvcResult = mockMvc.perform(post("/veiculo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clienteString)
                )
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andReturn();

        String responseExpected = mapper.writeValueAsString(new Response<VeiculoDTO>("CPF inválido!", null));

        Assertions.assertEquals(responseExpected, mvcResult.getResponse().getContentAsString());

    }

    @Test
    @DisplayName("Deletar veículo pela placa com sucesso")
    void deveriaDeletarVeiculoPelaPlacaSucesso() throws Exception {

        Mockito.when(veiculoRepository.findByPlaca(Mockito.anyString())).thenReturn(Optional.of(veiculoBD()));

        MvcResult mvcResult = mockMvc.perform(delete("/veiculo/{placa}", "XYZ-4578")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        Response<Boolean> response = Response.<Boolean>builder().message("Sucesso").detail(Boolean.TRUE).build();
        String responseExpected = mapper.writeValueAsString(response);
        Assertions.assertEquals(responseExpected, mvcResult.getResponse().getContentAsString());

    }

    @Test
    @DisplayName("Retorna todos os veículos")
    public void deveriaListarVeiculosSucesso() throws Exception {

        Mockito.when(veiculoRepository.findAll()).thenReturn(List.of(veiculoBD()));

        MvcResult mvcResult = mockMvc.perform(get("/veiculo/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<VeiculoDTO> veiculoDTOS = List.of(veiculoDTO());
        Response<List<VeiculoDTO>> response = Response.<List<VeiculoDTO>>builder().message("Sucesso").detail(veiculoDTOS).build();

        String responseExpected = mapper.writeValueAsString(response);

        Assertions.assertEquals(responseExpected, mvcResult.getResponse().getContentAsString());

    }

    @Test
    @DisplayName("Atualiza um veículo com sucesso")
    public void deveriaAtualizarVeiculoSucesso() throws Exception {

        String clienteString = mapper.writeValueAsString(veiculoDTO());

        Mockito.doCallRealMethod().when(validacaoPlaca).isPlacaValida(Mockito.anyString());
        Mockito.when(veiculoRepository.findByPlaca(Mockito.anyString())).thenReturn(Optional.of(veiculoBD()));
        Veiculo veiculoAtualizadoBD = veiculoBD();
        veiculoAtualizadoBD.setDisponivel(Boolean.FALSE);
        Mockito.when(veiculoRepository.save(Mockito.any(Veiculo.class))).thenReturn(veiculoAtualizadoBD);

        MvcResult mvcResult = mockMvc.perform(put("/veiculo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clienteString))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        Response<VeiculoDTO> response = new Response<VeiculoDTO>("Sucesso", veiculoAtualizadoDTO());
        String resultExpect = mapper.writeValueAsString(response);

        Assertions.assertEquals(resultExpect, result);

    }

    @Test
    @DisplayName("Retorna No Content ao atualizar um veículo que não existe na base de dados")
    public void deveriaRetornarNoContentAtualizarVeiculo() throws Exception {

        VeiculoDTO veiculoDTO = veiculoDTO();
        veiculoDTO.setPlaca("PRL-1234");
        String body = mapper.writeValueAsString(veiculoDTO);

        mockMvc.perform(put("/veiculo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNoContent())
                .andDo(print());

    }


    private static VeiculoDTO veiculoDTO(){
        VeiculoDTO veiculoDTO = new VeiculoDTO();
        veiculoDTO.setPlaca("XYZ-4578");
        veiculoDTO.setModelo("F40");
        veiculoDTO.setMarca("FERRARI");
        veiculoDTO.setDisponivel(Boolean.TRUE);
        veiculoDTO.setDataFabricacao(LocalDate.parse("2000-01-01"));
        return veiculoDTO;
    }

    private static VeiculoDTO veiculoAtualizadoDTO(){
        VeiculoDTO veiculoDTO = new VeiculoDTO();
        veiculoDTO.setPlaca("XYZ-4578");
        veiculoDTO.setModelo("F40");
        veiculoDTO.setMarca("FERRARI");
        veiculoDTO.setDisponivel(Boolean.FALSE);
        veiculoDTO.setDataFabricacao(LocalDate.parse("2000-01-01"));
        return veiculoDTO;
    }

    private static Veiculo veiculoBD() {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(1L);
        veiculo.setPlaca("XYZ-4578");
        veiculo.setModelo("F40");
        veiculo.setMarca("FERRARI");
        veiculo.setDisponivel(Boolean.TRUE);
        veiculo.setDataFabricacao(LocalDate.parse("2000-01-01"));
        return veiculo;
    }

}