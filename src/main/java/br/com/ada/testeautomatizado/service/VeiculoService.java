package br.com.ada.testeautomatizado.service;

import br.com.ada.testeautomatizado.dto.VeiculoDTO;
import br.com.ada.testeautomatizado.exception.PlacaInvalidaException;
import br.com.ada.testeautomatizado.exception.VeiculoNaoEncontradoException;
import br.com.ada.testeautomatizado.model.Veiculo;
import br.com.ada.testeautomatizado.repository.VeiculoRepository;
import br.com.ada.testeautomatizado.util.ResponseDTO;
import br.com.ada.testeautomatizado.util.ValidacaoPlaca;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VeiculoService {
    
    private static final String MSG_SUCESSO = "Sucesso";

    private final VeiculoRepository veiculoRepository;

    private final ValidacaoPlaca validacaoPlaca;
    
    public VeiculoService(VeiculoRepository veiculoRepository, ValidacaoPlaca validacaoPlaca) {
        this.veiculoRepository = veiculoRepository;
        this.validacaoPlaca = validacaoPlaca;
    }
    
    //C - CREATE
    public ResponseEntity<ResponseDTO<VeiculoDTO>> cadastrar(VeiculoDTO veiculoDTO) {
        try {
            this.validacaoPlaca.isPlacaValida(veiculoDTO.getPlaca());
            Veiculo veiculo = VeiculoDTO.dtoToVeiculo(veiculoDTO);
            this.veiculoRepository.save(veiculo);
            return ResponseEntity.ok()
                    .body(ResponseDTO.<VeiculoDTO>builder()
                            .message(MSG_SUCESSO)
                            .detail(veiculoDTO)
                            .build());
        } catch (PlacaInvalidaException placaInvalidaException) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ResponseDTO.<VeiculoDTO>builder()
                            .message(veiculoDTO.getPlaca() + placaInvalidaException.getMessage())
                            .detail(veiculoDTO)
                            .build());
        }
    }
    
    //R - READ.All
    public ResponseEntity<ResponseDTO<List<Veiculo>>> listarTodos() {
        List<Veiculo> veiculos = this.veiculoRepository.findAll().stream().toList();
        return ResponseEntity.ok()
                .body(ResponseDTO.<List<Veiculo>>builder()
                        .message(MSG_SUCESSO)
                        .detail(veiculos)
                        .build());
    }
    
    //R - READ
    public ResponseEntity<ResponseDTO<Veiculo>> listarPelaPlaca(String placa) {
        try {
            Veiculo veiculo = this.veiculoRepository.findByPlaca(placa)
                    .orElseThrow(VeiculoNaoEncontradoException::new);
            return ResponseEntity.ok()
                    .body(ResponseDTO.<Veiculo>builder()
                            .message(MSG_SUCESSO)
                            .detail(veiculo)
                            .build());
        } catch (VeiculoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ResponseDTO.<Veiculo>builder()
                            .message(placa + e.getMessage())
                            .build());
        }
    }
    
    //U - UPDATE
    public ResponseEntity<ResponseDTO<VeiculoDTO>> atualizar(VeiculoDTO veiculoDTO) {
        try {
            this.veiculoRepository.findByPlaca(veiculoDTO.getPlaca())
                    .orElseThrow(VeiculoNaoEncontradoException::new);
            Veiculo veiculo = VeiculoDTO.dtoToVeiculo(veiculoDTO);
            this.veiculoRepository.save(veiculo);
            return ResponseEntity.ok()
                    .body(ResponseDTO.<VeiculoDTO>builder()
                            .message(MSG_SUCESSO)
                            .detail(veiculoDTO)
                            .build());
        } catch (VeiculoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ResponseDTO.<VeiculoDTO>builder()
                            .message(veiculoDTO.getPlaca() + e.getMessage())
                            .detail(veiculoDTO)
                            .build());
        }
    }
    
    //D - DELETE
    public ResponseEntity<ResponseDTO<Boolean>> deletarVeiculoPelaPlaca(String placa) {
        try {
            this.veiculoRepository.delete(buscarVeiculoPelaPlaca(placa)
                    .orElseThrow(VeiculoNaoEncontradoException::new));
            return ResponseEntity.ok()
                    .body(ResponseDTO.<Boolean>builder()
                            .message(MSG_SUCESSO)
                            .detail(Boolean.TRUE)
                            .build());
        } catch (VeiculoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ResponseDTO.<Boolean>builder()
                            .message(placa + e.getMessage())
                            .detail(Boolean.FALSE)
                            .build());
        }
    }
    
    private Optional<Veiculo> buscarVeiculoPelaPlaca(String placa) {
        return this.veiculoRepository.findByPlaca(placa);
    }
}

