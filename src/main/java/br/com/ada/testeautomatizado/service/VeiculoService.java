package br.com.ada.testeautomatizado.service;

import br.com.ada.testeautomatizado.dto.VeiculoDTO;
import br.com.ada.testeautomatizado.exception.PlacaInvalidaException;
import br.com.ada.testeautomatizado.exception.VeiculoNaoEncontradoException;
import br.com.ada.testeautomatizado.model.Veiculo;
import br.com.ada.testeautomatizado.repository.VeiculoRepository;
import br.com.ada.testeautomatizado.util.Response;
import br.com.ada.testeautomatizado.util.ValidacaoPlaca;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private ValidacaoPlaca validacaoPlaca;

    public ResponseEntity<Response<VeiculoDTO>> cadastrar(VeiculoDTO veiculoDTO) {
        try {
            this.validacaoPlaca.isPlacaValida(veiculoDTO.getPlaca());
            Veiculo veiculo = VeiculoDTO.dtoToVeiculo(veiculoDTO);
            this.veiculoRepository.save(veiculo);
            return ResponseEntity.ok()
                    .body(Response.<VeiculoDTO>builder()
                            .message("Sucesso")
                            .detail(veiculoDTO)
                            .build());
        } catch (PlacaInvalidaException placaInvalidaException) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(Response.<VeiculoDTO>builder()
                            .message(veiculoDTO.getPlaca() + placaInvalidaException.getMessage())
                            .detail(veiculoDTO)
                            .build());
        }
    }

    public ResponseEntity<Response<Boolean>> deletarVeiculoPelaPlaca(String placa) {
        try {
            this.veiculoRepository.delete(buscarVeiculoPelaPlaca(placa)
                    .orElseThrow(VeiculoNaoEncontradoException::new));
            return ResponseEntity.ok()
                    .body(Response.<Boolean>builder()
                            .message("Sucesso")
                            .detail(Boolean.TRUE)
                            .build());
        } catch (VeiculoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Response.<Boolean>builder()
                            .message(placa + e.getMessage())
                            .detail(Boolean.FALSE)
                            .build());
        }
    }

    public ResponseEntity<Response<VeiculoDTO>> atualizar(VeiculoDTO veiculoDTO) {
        try {
            this.veiculoRepository.findByPlaca(veiculoDTO.getPlaca())
                    .orElseThrow(VeiculoNaoEncontradoException::new);
            Veiculo veiculo = VeiculoDTO.dtoToVeiculo(veiculoDTO);
            this.veiculoRepository.save(veiculo);
            return ResponseEntity.ok()
                    .body(Response.<VeiculoDTO>builder()
                            .message("Sucesso")
                            .detail(veiculoDTO)
                            .build());
        } catch (VeiculoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Response.<VeiculoDTO>builder()
                            .message(veiculoDTO.getPlaca() + e.getMessage())
                            .detail(veiculoDTO)
                            .build());
        }
    }

    public ResponseEntity<Response<List<Veiculo>>> listarTodos() {
        List<Veiculo> veiculos = this.veiculoRepository.findAll().stream().toList();
        return ResponseEntity.ok()
                .body(Response.<List<Veiculo>>builder()
                        .message("Sucesso")
                        .detail(veiculos)
                        .build());
    }

    private Optional<Veiculo> buscarVeiculoPelaPlaca(String placa) {
        return this.veiculoRepository.findByPlaca(placa);
    }
    
    public ResponseEntity<Response<Veiculo>> listarPelaPlaca(String placa) {
        try {
            Veiculo veiculo = this.veiculoRepository.findByPlaca(placa)
                    .orElseThrow(VeiculoNaoEncontradoException::new);
            return ResponseEntity.ok()
                    .body(Response.<Veiculo>builder()
                            .message("Sucesso")
                            .detail(veiculo)
                            .build());
        } catch (VeiculoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Response.<Veiculo>builder()
                            .message(placa + e.getMessage())
                            .build());
        }
    }
}

