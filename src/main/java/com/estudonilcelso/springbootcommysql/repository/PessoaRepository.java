package com.estudonilcelso.springbootcommysql.repository;

import com.estudonilcelso.springbootcommysql.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedNativeQuery;
import java.util.List;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    List<Pessoa> findByNomeContains(String name);
}