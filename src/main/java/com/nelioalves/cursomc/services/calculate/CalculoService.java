package com.nelioalves.cursomc.services.calculate;

public class CalculoService {

	public static Double calculaSubtotal(double preco, double desconto, double quantidade) {
		try {
			return (preco - desconto) * quantidade;

		} catch (NullPointerException e) {
			throw new RuntimeException("Erro ao realizar o cálculo");
		}
	}
}
