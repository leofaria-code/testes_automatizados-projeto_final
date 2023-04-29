package br.com.ada.testeautomatizado.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "tb_veiculo")
@Getter @Setter @Builder @ToString
@NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Veiculo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String placa;
    private String modelo;
    private String marca;
    
    private Boolean disponivel;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFabricacao;
}