package br.com.ada.testeautomatizado.controller;

import br.com.ada.testeautomatizado.dto.VeiculoDTO;
import br.com.ada.testeautomatizado.exception.PlacaInvalidaException;
import br.com.ada.testeautomatizado.exception.VeiculoNaoEncontradoException;
import br.com.ada.testeautomatizado.model.Veiculo;
import br.com.ada.testeautomatizado.repository.VeiculoRepository;
import br.com.ada.testeautomatizado.util.ResponseDTO;
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
    
    private static final String MSG_SUCESSO_EXPECTED = "Sucesso";
    
    private final Veiculo veiculoBD = veiculoBD();
    private final VeiculoDTO veiculoDTO = new VeiculoDTO(veiculoBD);

    @MockBean
    private ValidacaoPlaca validacaoPlaca;

    @MockBean
    private VeiculoRepository veiculoRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test   //C - CREATE: Sucesso
    @DisplayName("Cadastrar veículo com sucesso")
    void cadastrarSucesso() throws Exception {
        
        Mockito.doCallRealMethod().when(validacaoPlaca).isPlacaValida(Mockito.anyString());
        
        String veiculoDTOstring = mapper.writeValueAsString(veiculoDTO);
        
        MvcResult mvcResult = mockMvc.perform(post("/veiculo/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(veiculoDTOstring))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String resultActual = mvcResult.getResponse().getContentAsString();
        
        ResponseDTO<VeiculoDTO> responseDTOTest = ResponseDTO.<VeiculoDTO>builder()
                .message(MSG_SUCESSO_EXPECTED)
                .detail(veiculoDTO)
                .build();
        String responseExpected = mapper.writeValueAsString(responseDTOTest);

        Assertions.assertEquals(responseExpected, resultActual);

    }
    
    @Test   //C - CREATE: Erro, placa inválida
    @DisplayName("Erro ao cadastrar veículo com placa inválida")
    void deveriaRetornarErroCadastrarVeiculoPlacaInvalida() throws Exception {
        
        Mockito.doCallRealMethod().when(validacaoPlaca).isPlacaValida(Mockito.anyString());
        
        veiculoDTO.setPlaca("QWL23");
        String veiculoDTOstring = mapper.writeValueAsString(veiculoDTO);

        MvcResult mvcResult = mockMvc.perform(post("/veiculo/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(veiculoDTOstring))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andReturn();
        String resultActual = mvcResult.getResponse().getContentAsString();
        
        String exceptionMSG = new PlacaInvalidaException().getMessage();
        
        ResponseDTO<VeiculoDTO> responseDTOTest = ResponseDTO.<VeiculoDTO>builder()
                .message(veiculoDTO.getPlaca() + exceptionMSG)
                .detail(veiculoDTO)
                .build();
        String responseExpected = mapper.writeValueAsString(responseDTOTest);

        Assertions.assertEquals(responseExpected, resultActual);

    }
    
    @Test   //R - READ.All: Sucesso
    @DisplayName("Retorna todos os veículos")
    public void deveriaListarVeiculosSucesso() throws Exception {
        
        Mockito.when(veiculoRepository.findAll()).thenReturn(List.of(veiculoBD));
        
        MvcResult mvcResult = mockMvc.perform(get("/veiculo/todos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String resultActual = mvcResult.getResponse().getContentAsString();
        
        List<Veiculo> veiculos = List.of(veiculoBD);
        ResponseDTO<List<Veiculo>> responseDTOTest = ResponseDTO.<List<Veiculo>>builder()
                .message(MSG_SUCESSO_EXPECTED)
                .detail(veiculos)
                .build();
        String responseExpected = mapper.writeValueAsString(responseDTOTest);
        
        Assertions.assertEquals(responseExpected, resultActual);
        
    }
    
    @Test   //R - READ: Sucesso
    @DisplayName("Retorna um veículo específico")
    public void deveriaRetornarVeiculoPelaPlaca() throws Exception {
        
        Mockito.when(veiculoRepository.findByPlaca(Mockito.anyString())).thenReturn(Optional.of(veiculoBD));
        
        MvcResult mvcResult = mockMvc.perform(get("/veiculo/{placa}", veiculoBD.getPlaca())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String resultActual = mvcResult.getResponse().getContentAsString();
        
        ResponseDTO<Veiculo> responseDTOTest = ResponseDTO.<Veiculo>builder()
                .message(MSG_SUCESSO_EXPECTED)
                .detail(veiculoBD)
                .build();
        String responseExpected = mapper.writeValueAsString(responseDTOTest);
        
        Assertions.assertEquals(responseExpected, resultActual);
    }
    
    @Test   //R - READ: Erro, veículo não existe
    @DisplayName("Retorna No Content ao buscar um veículo que não existe na base de dados")
    public void deveriaRetornarNoContentBuscarVeiculoPelaPlaca() throws Exception {

        Mockito.when(veiculoRepository.findByPlaca(Mockito.anyString())).thenReturn(Optional.empty());
        
        MvcResult mvcResult = mockMvc.perform(get("/veiculo/{placa}", veiculoDTO.getPlaca())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn();
        String resultActual = mvcResult.getResponse().getContentAsString();
        
        String exceptionMSG = new VeiculoNaoEncontradoException().getMessage();
        
        ResponseDTO<Veiculo> responseDTOTest = ResponseDTO.<Veiculo>builder()
                .message(veiculoDTO.getPlaca() + exceptionMSG)
                .build();
        String responseExpected = mapper.writeValueAsString(responseDTOTest);
        
        Assertions.assertEquals(responseExpected, resultActual);
        
    }
    
    @Test   //U - UPDATE: Sucesso
    @DisplayName("Atualiza um veículo com sucesso")
    public void deveriaAtualizarVeiculoSucesso() throws Exception {
        
        Mockito.doCallRealMethod().when(validacaoPlaca).isPlacaValida(Mockito.anyString());
        Mockito.when(veiculoRepository.findByPlaca(Mockito.anyString())).thenReturn(Optional.of(veiculoBD));
        
        Veiculo veiculoAtualizadoBD = veiculoBD;
        veiculoAtualizadoBD.setDisponivel(Boolean.FALSE);
        
        VeiculoDTO veiculoAtualizadoDTO = new VeiculoDTO(veiculoAtualizadoBD);
        
        Mockito.when(veiculoRepository.save(Mockito.any(Veiculo.class))).thenReturn(veiculoAtualizadoBD);
        
        String veiculoAtualizadostring = mapper.writeValueAsString(veiculoAtualizadoBD);
        
        MvcResult mvcResult = mockMvc.perform(put("/veiculo/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(veiculoAtualizadostring))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String resultActual = mvcResult.getResponse().getContentAsString();
        
        ResponseDTO<VeiculoDTO> responseDTOTest = ResponseDTO.<VeiculoDTO>builder()
                .message(MSG_SUCESSO_EXPECTED)
                .detail(veiculoAtualizadoDTO)
                .build();
        String responseExpected = mapper.writeValueAsString(responseDTOTest);

        Assertions.assertEquals(responseExpected, resultActual);

    }
    
    @Test   //U - UPDATE: Erro, veículo não existe
    @DisplayName("Retorna No Content ao atualizar um veículo que não existe na base de dados")
    public void deveriaRetornarNoContentAtualizarVeiculo() throws Exception {
    
        Mockito.when(veiculoRepository.findByPlaca(Mockito.anyString())).thenReturn(Optional.empty());
        
        String veiculoDTOstring = mapper.writeValueAsString(veiculoDTO);
        
        MvcResult mvcResult = mockMvc.perform(put("/veiculo/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(veiculoDTOstring))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn();
        String resultActual = mvcResult.getResponse().getContentAsString();
        
        String exceptionMSG = new VeiculoNaoEncontradoException().getMessage();
        
        ResponseDTO<VeiculoDTO> responseDTOTest = ResponseDTO.<VeiculoDTO>builder()
                .message(veiculoDTO.getPlaca() + exceptionMSG)
                .detail(veiculoDTO)
                .build();
        String responseExpected = mapper.writeValueAsString(responseDTOTest);
        
        Assertions.assertEquals(responseExpected, resultActual);
        
    }
    
    @Test   //D - DELETE: Sucesso
    @DisplayName("Deletar veículo pela placa com sucesso")
    void deveriaDeletarVeiculoPelaPlacaSucesso() throws Exception {
        
        Mockito.doCallRealMethod().when(validacaoPlaca).isPlacaValida(Mockito.anyString());
        
        Mockito.when(veiculoRepository.findByPlaca(Mockito.anyString())).thenReturn(Optional.of(veiculoBD));
        
        MvcResult mvcResult = mockMvc.perform(delete("/veiculo/{placa}", veiculoBD.getPlaca())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String resultActual = mvcResult.getResponse().getContentAsString();
        
        ResponseDTO<Boolean> responseDTOTest = ResponseDTO.<Boolean>builder()
                .message(MSG_SUCESSO_EXPECTED)
                .detail(Boolean.TRUE)
                .build();
        String responseExpected = mapper.writeValueAsString(responseDTOTest);
        
        Assertions.assertEquals(responseExpected, resultActual);
        
    }
    
    @Test   //D - DELETE: Erro, veículo não existe
    @DisplayName("Retorna No Content ao deletar um veículo que não existe na base de dados")
    public void deveriaRetornarNoContentDeletarVeiculo() throws Exception {
        
        veiculoDTO.setPlaca("PRL-1234");
        
        Mockito.doCallRealMethod().when(validacaoPlaca).isPlacaValida(Mockito.anyString());
        
        String veiculoDTOstring = mapper.writeValueAsString(veiculoDTO);
        
        MvcResult mvcResult = mockMvc.perform(delete("/veiculo/{placa}", "PRL-1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(veiculoDTOstring))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn();
        String resultActual = mvcResult.getResponse().getContentAsString();
        
        String exceptionMSG = new VeiculoNaoEncontradoException().getMessage();
        
        ResponseDTO<Boolean> responseDTOTest = ResponseDTO.<Boolean>builder()
                .message(veiculoDTO.getPlaca() + exceptionMSG)
                .detail(Boolean.FALSE)
                .build();
        String responseExpected = mapper.writeValueAsString(responseDTOTest);
        
        Assertions.assertEquals(responseExpected, resultActual);
        
    }
    
    private static Veiculo veiculoBD() {
        return Veiculo.builder()
                .id(1L)
                .placa("XYZ-4E78")
                .marca("FERRARI")
                .modelo("F40")
                .dataFabricacao(LocalDate.parse("2000-01-01"))
                .disponivel(Boolean.TRUE)
                .build();
    }
    
}