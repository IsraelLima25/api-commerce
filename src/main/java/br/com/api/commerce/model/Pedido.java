package br.com.api.commerce.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.api.commerce.indicador.FormaPagamentoIndicador;
import br.com.api.commerce.indicador.StatusPedidoIndicador;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_pedido")
public class Pedido {

	@Id
	@Column(name = "id")
	private UUID id;
	
	@Column(name = "data_hora_local", nullable = false)
	private LocalDateTime instante;
	
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusPedidoIndicador status;
	
	@Column(name = "forma_pagamento", nullable = false)
	@Enumerated(EnumType.STRING)
	private FormaPagamentoIndicador formaPagamento;
	
	@ManyToOne
	@JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
	private Cliente cliente;
	
	@OneToMany(mappedBy = "id.pedido", cascade = CascadeType.ALL)
	private List<ItemPedido> itens = new ArrayList<>();
	
	@Column(name = "valor_total_itens", nullable = false)
	private BigDecimal valorTotalPedido;

	@Column(name = "taxa", nullable = false)
	private BigDecimal valorTaxa;
	@Column(name = "valor_total_taxado", nullable = false)
	private BigDecimal valorTotalTaxado;
	
	public Pedido() {
		this.id = UUID.randomUUID();
		this.instante = LocalDateTime.now();
		this.status = StatusPedidoIndicador.AGUARDANDO_PAGAMENTO;
		this.valorTotalPedido = BigDecimal.ZERO;
		this.valorTaxa = BigDecimal.ZERO;
		this.valorTotalTaxado = BigDecimal.ZERO;
	}

	public UUID getId() {
		return id;
	}
	
	public List<ItemPedido> getItens() {
		return itens;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public LocalDateTime getInstante() {
		return instante;
	}
	
	public BigDecimal getValorTotalPedido() {
		return this.itens.stream().map(itemPedido -> itemPedido.getId().getProduto().getPrecoUnitario()
				.multiply(new BigDecimal(itemPedido.getQuantidade()))).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public FormaPagamentoIndicador getFormaPagamento() {
		return formaPagamento;
	}

	public BigDecimal getValorTaxa() {
		return this.getValorTotalPedido().multiply(this.formaPagamento.valorTaxa()).setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal getValorTotalTaxado() {
		return this.getValorTotalPedido().add(getValorTaxa());
	}

	public void adicionarItem(ItemPedido item) {
		this.itens.add(item);
	}

	public void adicionarCliente(Cliente cliente){
		this.cliente=cliente;
	}

	public void adicionarFormaPagamento(FormaPagamentoIndicador formaPagamento){
		this.formaPagamento = formaPagamento;
	}

}
