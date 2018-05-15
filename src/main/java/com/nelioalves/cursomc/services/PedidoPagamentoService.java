package com.nelioalves.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.repositories.PedidoPagamentoRepository;

import javassist.tools.rmi.ObjectNotFoundException;

@Service
public class PedidoPagamentoService {

	@Autowired
	private PedidoPagamentoRepository pedidoPagamentoRepository;

	public Pedido find(Integer id) throws ObjectNotFoundException {
		Optional<Pedido> obj = pedidoPagamentoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
}
