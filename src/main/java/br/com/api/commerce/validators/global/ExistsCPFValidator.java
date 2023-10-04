package br.com.api.commerce.validators.global;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraints.UUID;
import org.springframework.util.Assert;

import java.util.List;

public class ExistsCPFValidator implements ConstraintValidator<ExistsCPF, String> {

    @PersistenceContext
    private EntityManager manager;

    private String domainAttribute;
    private Class<?> klass;

    @Override
    public void initialize(ExistsCPF params) {
        domainAttribute = params.fieldName();
        klass = params.domainClass();
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext constraintValidatorContext) {

        if(cpf == null) {
            return true;
        }
        Query query = manager.createQuery("select 1 from " + klass.getName() + " where " + domainAttribute + "=:value");
        query.setParameter("value", cpf);
        List<?> list = query.getResultList();
        Assert.state(list.size() <= 1, "Erro grave!! Foi encontrado mais de um registro com o id="+cpf);

        return !list.isEmpty();
    }
}
