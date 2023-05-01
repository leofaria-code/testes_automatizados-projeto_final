package br.com.ada.testeautomatizado.controller;


import br.com.ada.testeautomatizado.dto.VeiculoDTO;
import br.com.ada.testeautomatizado.model.Veiculo;
import br.com.ada.testeautomatizado.service.VeiculoService;
import br.com.ada.testeautomatizado.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController //CRUD
@RequestMapping("/veiculo")
public class VeiculoController {

    private final VeiculoService veiculoService;
    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }
    
    @PostMapping("/")   //C - CREATE
    public ResponseEntity<Response<VeiculoDTO>> cadastrar(@RequestBody VeiculoDTO veiculoDTO) {
        return this.veiculoService.cadastrar(veiculoDTO);
    }
    
    @GetMapping("/todos")   //R - READ
    public ResponseEntity<Response<List<Veiculo>>> listarTodos(){
        return this.veiculoService.listarTodos();
    }
    
    @GetMapping("/{placa}")
    public ResponseEntity<Response<Veiculo>> listarVeiculoPelaPlaca (@PathVariable("placa") String placa) {
//        return null;
        return this.veiculoService.listarPelaPlaca(placa);
    }

    @PutMapping("/")    //U - UPDATE
    public ResponseEntity<Response<VeiculoDTO>> atualizar(@RequestBody VeiculoDTO veiculoDTO) {
        return this.veiculoService.atualizar(veiculoDTO);
    }
    
    @DeleteMapping("/{placa}")  //D - DELETE
    public ResponseEntity<Response<Boolean>> deletarVeiculoPelaPlaca(@PathVariable("placa") String placa) {
        return this.veiculoService.deletarVeiculoPelaPlaca(placa);
    }

}
