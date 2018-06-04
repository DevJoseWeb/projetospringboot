package com.nelioalves.cursomc.services;

import java.util.InputMismatchException;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.domain.enums.TipoCliente;
import com.nelioalves.cursomc.repositories.ClienteRepository;

import javassist.tools.rmi.ObjectNotFoundException;

@Service
public class ClienteValidate {
	@Autowired
	private ClienteRepository clienteRepository;

	public boolean verificaCliente(Cliente obj) throws ObjectNotFoundException {
		if (verificaEmail(obj)) {
			if (verificaNome(obj)) {
				if (verificaCaractere(obj)) {
					if (validaEmail(obj)) {
						if ((obj.getTipo().equals(TipoCliente.PESSOAFISICA))) {
							if (verificaCpf(obj)) {
								if (verificaCadastro(obj)) {
									return true;
								}
								throw new ObjectNotFoundException("CPF já cadastrado! Id: " + obj.getCpfOuCnpj()
										+ ", Tipo: " + Cliente.class.getName());
							}
							throw new ObjectNotFoundException(
									"CPF inválido! Id: " + obj.getEmail() + ", Tipo: " + Cliente.class.getName());
						} else if (obj.getTipo().equals(TipoCliente.PESSOAJURIDICA)) {
							if (verificaCnpj(obj)) {
								if (verificaCadastroJuridico(obj)) {
									return true;
								}
								throw new ObjectNotFoundException("CNPJ já cadastrado");
							}
							throw new ObjectNotFoundException("CNPJ inválido");
						}
					}
					throw new ObjectNotFoundException(
							"E-mail inválido! Id: " + obj.getEmail() + ", Tipo: " + Cliente.class.getName());
				}
				throw new ObjectNotFoundException("O campo nome deve conter apenas letras! Id: " + obj.getNome()
						+ ", Tipo: " + Cliente.class.getName());
			}
			throw new ObjectNotFoundException("O campo nome deve conter de 5 a 20 caracteres e não deve ser vázio! Id: "
					+ obj.getNome() + ", Tipo: " + Cliente.class.getName());
		}
		throw new ObjectNotFoundException("E-mail já cadastrado");
	}


	public boolean verificaNome(Cliente obj) throws ObjectNotFoundException {
		if (obj.getNome().trim().equals("")) {
			return false;
		}

		if (obj.getNome().length() < 5 || obj.getNome().length() > 20) {
			return false;
		}
		return true;
	}

	boolean verificaCaractere(Cliente obj) {
		String[] caractere = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "@", "!", "*", "-", "=", "/", "?",
				"_" };
		for (String caracteres : caractere) {
			if (obj.getNome().contains(caracteres)) {
				return false;
			}
		}
		return true;
	}

	public boolean validaEmail(Cliente obj) {
		boolean isEmailIdValid = false;
		if (obj.getEmail() != null && obj.getEmail().length() > 0) {
			String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
			Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
			java.util.regex.Matcher matcher = pattern.matcher(obj.getEmail());
			if (matcher.matches()) {
				isEmailIdValid = true;
			}
		}
		return isEmailIdValid;
	}

	public boolean verificaCadastro(Cliente obj) {
		List<Cliente> cliente = clienteRepository.findAll();
		for (Cliente clientes : cliente) {
			if (obj.getCpfOuCnpj().equals(clientes.getCpfOuCnpj())) {
				return false;
			}
		}
		return true;
	}

	public boolean verificaEmail(Cliente obj) {
		List<Cliente> cliente = clienteRepository.findAll();
		for (Cliente clientes : cliente) {
			if (obj.getEmail().equals(clientes.getEmail())) {
				return false;
			}
		}
		return true;
	}

	public boolean verificaCadastroJuridico(Cliente obj) {
		List<Cliente> cliente = clienteRepository.findAll();
		for (Cliente clientes : cliente) {
			if (obj.getCpfOuCnpj().equals(clientes.getCpfOuCnpj())) {
				return false;
			}
		}
		return true;
	}

	public boolean verificaCpf(Cliente obj) {
		int digito10 = retornaCalculo(obj.getCpfOuCnpj().substring(0, 9).toCharArray());
		int digito11 = retornaCalculo(obj.getCpfOuCnpj().substring(0, 10).toCharArray());
		return obj.getCpfOuCnpj().substring(9, 11).equals((digito10 + "" + digito11));
	}

	private static int retornaCalculo(char[] cpfArray) {
		int fator = cpfArray.length + 1;
		int soma = 0;
		for (int i = 0; i < cpfArray.length; i++) {
			soma += Character.getNumericValue(cpfArray[i]) * fator--;
		}
		int digito = 0;
		int mod = soma % 11;
		if (mod == 0 || mod == 1) {
			digito = 0;
		} else {
			digito = 11 - mod;
		}
		return digito;
	}

	public boolean verificaCnpj(Cliente obj) {
		if (obj.getCpfOuCnpj().equals("00000000000000") || obj.getCpfOuCnpj().equals("11111111111111")
				|| obj.getCpfOuCnpj().equals("22222222222222") || obj.getCpfOuCnpj().equals("33333333333333")
				|| obj.getCpfOuCnpj().equals("44444444444444") || obj.getCpfOuCnpj().equals("55555555555555")
				|| obj.getCpfOuCnpj().equals("66666666666666") || obj.getCpfOuCnpj().equals("77777777777777")
				|| obj.getCpfOuCnpj().equals("88888888888888") || obj.getCpfOuCnpj().equals("99999999999999")
				|| (obj.getCpfOuCnpj().length() != 14))
			return (false);

		char dig13, dig14;
		int sm, i, r, num, peso;

		try {
			sm = 0;
			peso = 2;

			for (i = 11; i >= 0; i--) {
				num = (int) (obj.getCpfOuCnpj().charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig13 = '0';
			else
				dig13 = (char) ((11 - r) + 48);

			sm = 0;
			peso = 2;
			for (i = 12; i >= 0; i--) {
				num = (int) (obj.getCpfOuCnpj().charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig14 = '0';
			else
				dig14 = (char) ((11 - r) + 48);

			if ((dig13 == obj.getCpfOuCnpj().charAt(12)) && (dig14 == obj.getCpfOuCnpj().charAt(13)))
				return (true);
			else
				return (false);
		} catch (InputMismatchException erro) {
			return (false);
		}
	}

}
