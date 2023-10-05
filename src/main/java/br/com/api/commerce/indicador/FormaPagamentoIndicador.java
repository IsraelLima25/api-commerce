package br.com.api.commerce.indicador;

import java.math.BigDecimal;

public enum FormaPagamentoIndicador {
	DEBITO {
		@Override
		public BigDecimal valorTaxa() {
			return new BigDecimal(0.1);
		}
	}, CREDITO {
		@Override
		public BigDecimal valorTaxa() {
			return new BigDecimal(0.2);
		}
	}, PIX {
		@Override
		public BigDecimal valorTaxa() {
			return BigDecimal.ZERO;
		}
	};

	public abstract BigDecimal valorTaxa();

}
