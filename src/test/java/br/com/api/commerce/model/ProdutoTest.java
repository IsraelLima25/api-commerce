package br.com.api.commerce.model;

import br.com.api.commerce.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class ProdutoTest {

    Produto produto;

    @BeforeEach
    void setupInicial(){
        Produto.Builder builder = new Produto.Builder();
        produto = builder.setDescricao("Core I7")
                .setPrecoUnitario(new BigDecimal(3000))
                .setQuantidade(10)
                .build();
    }

    @Test
    @DisplayName("Deve lançar exception para quantidade comprada maior que a quantidade de estoque")
    void naoDeveTerEstoqueProduto() {
        assertThrows(BusinessException.class, () -> {
            this.produto.temEstoque(11);
        });
    }

    @Test
    @DisplayName("Deve abater quantidade comprada no estoque")
    void deveAbaterEstoque() {
        this.produto.abaterEstoque(5);
        assertEquals(5, produto.getQuantidade());
    }
}