package cn.org.awcp.formdesigner.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import cn.org.awcp.metadesigner.vo.MetaModelVO;

public class ModelValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return MetaModelVO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "id", null, "id is empty.");
		MetaModelVO entity = (MetaModelVO) target;
		if (null == entity.getModelCode() || "".equals(entity.getModelCode())) {
            errors.rejectValue("code", null, "code is empty.");
        }
	}

}
