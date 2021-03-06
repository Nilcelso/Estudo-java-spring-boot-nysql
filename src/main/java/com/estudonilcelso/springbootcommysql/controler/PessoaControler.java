package com.estudonilcelso.springbootcommysql.controler;

import com.estudonilcelso.springbootcommysql.controler.dto.PessoaRq;
import com.estudonilcelso.springbootcommysql.controler.dto.PessoaRs;
import com.estudonilcelso.springbootcommysql.model.Pessoa;
import com.estudonilcelso.springbootcommysql.repository.PessoaCustomRepository;
import com.estudonilcelso.springbootcommysql.repository.PessoaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pessoa")
class PessoaController {

    private final PessoaRepository pessoaRepository;
    private final PessoaCustomRepository pessoaCustomRepository;

    public PessoaController(PessoaRepository pessoaRepository, PessoaCustomRepository pessoaCustomRepository) {
        this.pessoaRepository = pessoaRepository;
        this.pessoaCustomRepository = pessoaCustomRepository;
    }

    @GetMapping("/")
    public List<PessoaRs> findAll() {
        var pessoas = pessoaRepository.findAll();
        return pessoas
                .stream()
                .map(PessoaRs::converter)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PessoaRs findById(@PathVariable("id") Long id) {
        var pessoa = pessoaRepository.getOne(id);
        return PessoaRs.converter(pessoa);
    }

    @PostMapping("/")
    public void savePerson(@RequestBody PessoaRq pessoa) {
        var p = new Pessoa();
        p.setNome(pessoa.getNome());
        p.setSobrenome(pessoa.getSobrenome());
        pessoaRepository.save(p);
    }

    @PutMapping("/{id}")
    public void updatePerson(@PathVariable("id") Long id, @RequestBody PessoaRq pessoa) throws Exception {
        var p = pessoaRepository.findById(id);

        if (p.isPresent()) {
            var pessoaSave = p.get();
            pessoaSave.setNome(pessoa.getNome());
            pessoaSave.setSobrenome(pessoa.getSobrenome());
            pessoaRepository.save(pessoaSave);
        } else {
            throw new Exception("Pessoa N??o encontrada");
        }
    }

    @GetMapping("/filter")
    public List<PessoaRs> findPersonByName(@RequestParam("name") String name) {
        return this.pessoaRepository.findByNomeContains(name)
                .stream()
                .map(PessoaRs::converter)
                .collect(Collectors.toList());
    }

    @GetMapping("/filter/custom")
    public List<PessoaRs> findPersonByCustom(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "sobrenome", required = false) String sobrenome
    ) {
        return this.pessoaCustomRepository.find(id, name, sobrenome)
                .stream()
                .map(PessoaRs::converter)
                .collect(Collectors.toList());
    }

}