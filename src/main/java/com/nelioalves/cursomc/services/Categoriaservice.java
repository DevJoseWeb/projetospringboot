package com.nelioalves.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.repositories.CategoriaRepository;

@Service
public class Categoriaservice {
	
	@Autowired
	private CategoriaRepository repository;
	public Optional<Categoria> buscar(Integer id) {
		Optional<Categoria> obj = repository.findById(id);
		
		return obj;
	}

}
