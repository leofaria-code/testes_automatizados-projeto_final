package br.com.ada.testeautomatizado.controller;


import br.com.ada.testeautomatizado.dto.VeiculoDTO;
import br.com.ada.testeautomatizado.service.VeiculoService;
import br.com.ada.testeautomatizado.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/veiculo")
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;


    @GetMapping("/todos")
    public ResponseEntity<Response<List<VeiculoDTO>>> listarTodos(){
        return this.veiculoService.listarTodos();
    }

    @PostMapping("/")
    public ResponseEntity<Response<VeiculoDTO>> cadastrar(@RequestBody VeiculoDTO veiculoDTO) {
        return this.veiculoService.cadastrar(veiculoDTO);
    }

    @DeleteMapping("/{placa}")
    public ResponseEntity<Response<Boolean>> deletarVeiculoPelaPlaca(@PathVariable("placa") String placa) {
        return this.veiculoService.deletarVeiculoPelaPlaca(placa);
    }

    @PutMapping("/")
    public ResponseEntity<Response<VeiculoDTO>> atualizar(@RequestBody VeiculoDTO veiculoDTO) {
        return this.veiculoService.atualizar(veiculoDTO);
    }

}
