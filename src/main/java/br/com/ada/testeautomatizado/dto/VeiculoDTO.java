package br.com.ada.testeautomatizado.dto;

import br.com.ada.testeautomatizado.model.Veiculo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VeiculoDTO {
    private String placa;
    private String marca;
    private String modelo;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFabricacao;
    
    private Boolean disponivel;
    
    public VeiculoDTO(Veiculo veiculoModel) {
        BeanUtils.copyProperties(veiculoModel, this);
    }
    
    public static Veiculo dtoToVeiculo(VeiculoDTO veiculoDTO) {
        return Veiculo.builder()
                .placa(veiculoDTO.getPlaca())
                .marca(veiculoDTO.getMarca())
                .modelo(veiculoDTO.getModelo())
                .dataFabricacao(veiculoDTO.getDataFabricacao())
                .disponivel(veiculoDTO.getDisponivel())
                .build();
    }
}

