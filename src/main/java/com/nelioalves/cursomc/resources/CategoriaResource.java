package com.nelioalves.cursomc.resources;

import java.net.URI;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.dto.CategoriaDTO;
import com.nelioalves.cursomc.services.CategoriaService;

import javassist.tools.rmi.ObjectNotFoundException;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService categoriaService;
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> find(@PathVariable Integer id) throws ObjectNotFoundException {
		Categoria obj = categoriaService.find(id);
		return ResponseEntity.ok().body(obj);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@RequestBody Categoria obj) {
		obj = (Categoria) categoriaService.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody Categoria obj, @PathVariable Integer id)
			throws ObjectNotFoundException {
		obj.setId(id);
		obj = (Categoria) categoriaService.update(obj);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) throws ObjectNotFoundException {
		categoriaService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	// @RequestMapping (method = RequestMethod.GET)
	// public ResponseEntity <List<Categoria>> findAll() throws
	// ObjectNotFoundException {
	// List<Categoria> list = categoriaService.findAll();
	// return ResponseEntity.ok().body(list);
	// }
	
	@RequestMapping(method = RequestMethod.GET)
	public List<CategoriaDTO> findAll() throws ObjectNotFoundException, UnexpectedException {
		List<CategoriaDTO> listDto = new ArrayList<CategoriaDTO>();
		List<Categoria> listCategoria = categoriaService.findAll();

		for (Categoria categoria : listCategoria) {
			listDto.add(converterCategoriaToDTO(categoria));
		}

		return listDto;
	}

	private CategoriaDTO converterCategoriaToDTO(Categoria categoria) throws UnexpectedException {
		CategoriaDTO dto = new CategoriaDTO();
		dto.setId(categoria.getId());
		dto.setNome(categoria.getNome());

		return dto;
	}
}
	
	
  
