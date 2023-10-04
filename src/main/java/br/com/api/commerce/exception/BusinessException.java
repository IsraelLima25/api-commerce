package br.com.api.commerce.exception;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String campo;
	private String mensagem;
	
	public BusinessException(String campo, String mensagem) {
		this.campo = campo;
		this.mensagem = mensagem;
	}

	public String getCampo() {
		return campo;
	}

	public String getMensagem() {
		return mensagem;
	}
}
