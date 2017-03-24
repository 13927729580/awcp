package org.szcloud.framework.formdesigner.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.szcloud.framework.metadesigner.vo.MetaModelVO;

public class ModelValidator implements Validator {

	public boolean supports(Class<?> clazz) {
		return MetaModelVO.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "id", null, "id is empty.");
		MetaModelVO entity = (MetaModelVO) target;
		if (null == entity.getModelCode() || "".equals(entity.getModelCode()))
			errors.rejectValue("code", null, "code is empty.");
	}

}
