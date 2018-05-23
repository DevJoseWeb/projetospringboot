package com.nelioalves.cursomc.services;

import java.rmi.UnexpectedException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.dto.CategoriaDTO;
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

	public Object insert(Categoria obj) throws ObjectNotFoundException {
		obj.setId(null);
		if (verificaNome(obj)) {
			return categoriaRepository.save(obj);
		}
		throw new ObjectNotFoundException(
				"O campo nome deve conter apenas letras! Id: " + obj + ", Tipo: " + Categoria.class.getName());
	}

	public boolean verificaNome(Categoria obj) throws ObjectNotFoundException {
		if (obj.getNome().trim().equals("")) {
			return false;
		}

		if (obj.getNome().length() < 5 || obj.getNome().length() > 20) {
			return false;
		}

		List<Categoria> categoria = categoriaRepository.findAll();
		for (Categoria categorias : categoria) {
			if (obj.getNome().equals(categorias.getNome())) {
				return false;
			}
		}

		String[] caractere = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "@", "!", "*", "-", "=", "/", "?",
				"_" };
		for (String caracteres : caractere) {
			if (obj.getNome().contains(caracteres)) {
				return false;
			}
		}
		return true;
	}

	public  Categoria update(Categoria obj) throws ObjectNotFoundException  {

			Categoria newObj = find(obj.getId());
			updateData(newObj, obj);
			return categoriaRepository.save(newObj);
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
	
	public void updateData(Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());
	}

	public CategoriaDTO converterCategoriaToDTO(Categoria categoria) throws UnexpectedException {
		CategoriaDTO dto = new CategoriaDTO();
		dto.setId(categoria.getId());
		dto.setNome(categoria.getNome());
		
		return dto;
	}

	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return categoriaRepository.findAll(pageRequest);

	}
}