package br.com.api.commerce.utilities;

public class Mask {
	
	public static String applyCpf(String cpf) {
		return cpf.substring(0, 3).toString().concat("***");
	}
}
