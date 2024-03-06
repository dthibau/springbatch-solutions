package org.formation.file;

import org.formation.model.InputProduct;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

public class ProductValidator implements Validator<InputProduct> {

	@Override
	public void validate(InputProduct product) throws ValidationException {
		
		if ( product.getNom() == null || product.getNom().length() == 0 ) {
			throw new ValidationException("Le nom doit être renseigné");
		}
		
		if ( product.getReference() == null || product.getReference().length() == 0 || product.getReference().length() > 5 ) {
			throw new ValidationException("Le nom doit être renseigné");
		}
		
	}

}
