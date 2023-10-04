package br.com.api.commerce.validators.global;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

public class ExistsIDValidator implements ConstraintValidator<ExistsID, UUID> {

    @PersistenceContext
    private EntityManager manager;

    private String domainAttribute;
    private Class<?> klass;

    @Override
    public void initialize(ExistsID params) {
        domainAttribute = params.fieldName();
        klass = params.domainClass();
    }

    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext constraintValidatorContext) {

        if(id == null) {
            return true;
        }
        Query query = manager.createQuery("select 1 from " + klass.getName() + " where " + domainAttribute + "=:value");
        query.setParameter("value", id);
        List<?> list = query.getResultList();
        Assert.state(list.size() <= 1, "Erro grave!! Foi encontrado mais de um registro com o id="+id);

        return !list.isEmpty();
    }
}
