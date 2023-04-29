package br.com.ada.testeautomatizado.service;

import br.com.ada.testeautomatizado.dto.VeiculoDTO;
import br.com.ada.testeautomatizado.model.Veiculo;
import br.com.ada.testeautomatizado.repository.VeiculoRepository;
import br.com.ada.testeautomatizado.util.Response;
import br.com.ada.testeautomatizado.util.ValidacaoPlaca;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        return null;
    }

    public ResponseEntity<Response<Boolean>> deletarVeiculoPelaPlaca(String placa) {
        return null;
    }

    public ResponseEntity<Response<VeiculoDTO>> atualizar(VeiculoDTO veiculoDTO) {
        return null;
    }

    public ResponseEntity<Response<List<VeiculoDTO>>> listarTodos() {
        return null;
    }

    private Optional<Veiculo> buscarVeiculoPelaPlaca(String placa) {
        return this.veiculoRepository.findByPlaca(placa);
    }
}

