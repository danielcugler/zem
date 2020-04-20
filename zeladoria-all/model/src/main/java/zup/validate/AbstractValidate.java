package zup.validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import zup.messages.IMessages;
import zup.messages.Messages;

public abstract class AbstractValidate<T> {

	private Class<T> clazz;

	public AbstractValidate(Class<T> validateClass) {

		clazz = validateClass;
	}

	public Set<ConstraintViolation<T>> valida(T t) {

		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();

		Set<ConstraintViolation<T>> validationErrors = validator.validate(t);
		if (!validationErrors.isEmpty()) {
			for (ConstraintViolation<T> error : validationErrors) {
				System.out.println(
						error.getMessageTemplate() + "::" + error.getPropertyPath() + "::" + error.getMessage());

			}
		}
		return validationErrors;
	}

	public Map<String, String> errosBean(T t) {
		Map<String, String> mapaErros = new HashMap<String, String>();
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();

		Set<ConstraintViolation<T>> validationErrors = validator.validate(t);

		if (!validationErrors.isEmpty()) {
			for (ConstraintViolation<T> error : validationErrors) {
				mapaErros.put(error.getPropertyPath().toString(), error.getMessage());

			}
		}
		return mapaErros;
	}

	public String errosStr(T t) {
		Map<String, String> m = errosBean(t);
		String menssagensErro = "";
		for (Map.Entry<String, String> entry : m.entrySet()) {
			if (entry.getValue().equals(IMessages.NULL_FIELDS))
				menssagensErro += (Messages.getInstance().getMessage(entry.getValue(), entry.getKey()));
			else if (entry.getValue().equals(IMessages.INVALID_FIELDS))
				menssagensErro += (Messages.getInstance().getMessage(entry.getValue(), entry.getKey()));
			else if (entry.getValue().equals(IMessages.SIZE_LIMIT_EXCEEDS))
				menssagensErro += (Messages.getInstance().getMessage(entry.getValue(), entry.getKey()));
			else
				menssagensErro += (Messages.getInstance().getMessage(entry.getValue()));
		}
		return menssagensErro;
	}

}