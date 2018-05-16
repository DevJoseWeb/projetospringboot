package com.nelioalves.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.repositories.CategoriaRepository;

import javassist.tools.rmi.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository<?> categoriaRepository;

	public Categoria find(Integer id) throws ObjectNotFoundException {
		Optional<Categoria> obj = categoriaRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}

	public Object insert(Categoria obj) {
		obj.setId(null);
		return categoriaRepository.save(obj);
	}

	public Object update(Categoria obj) throws ObjectNotFoundException {
		try {
			find(obj.getId());
			return categoriaRepository.save(obj);

		} catch (NullPointerException e) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + obj + ", Tipo: " + Categoria.class.getName());
		}
	  }

	public void delete(Integer id) throws ObjectNotFoundException {
		{
			String nomeCategoria = null;
			String nomeProduto = null;
			try {
				nomeCategoria = categoriaRepository.getOne(id).getNome();
				nomeProduto = categoriaRepository.getOne(id).getProdutos().toString();
				find(id);
				categoriaRepository.deleteById(id);
			} catch (NullPointerException e) {
				throw new ObjectNotFoundException("A Categoria " + nomeCategoria + " possui os produtos " + nomeProduto
					  + " e não pode ser excluida " + Categoria.class.getName());
			}
		  }
	    }
	
	public List<Categoria> findAll() throws ObjectNotFoundException {
		try {
			return categoriaRepository.findAll();
		} catch (NullPointerException e) {
			throw new ObjectNotFoundException("Não foram encontradas categorias");
		}
	  }
}