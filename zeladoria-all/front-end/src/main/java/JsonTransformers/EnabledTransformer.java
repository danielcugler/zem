package JsonTransformers;

import flexjson.BasicType;
import flexjson.TypeContext;
import flexjson.transformer.AbstractTransformer;
import zup.enums.Enabled;

public class EnabledTransformer extends AbstractTransformer {
	public void transform(Object object) {

		boolean setContext = false;

		TypeContext typeContext = getContext().peekTypeContext();

		String propertyName = typeContext != null ? typeContext.getPropertyName() : "";

		if (typeContext == null || typeContext.getBasicType() != BasicType.OBJECT) {
			typeContext = getContext().writeOpenObject();
			setContext = true;
		}

		Enabled tipo = (Enabled) object;

		if (!typeContext.isFirst())
			getContext().writeComma();

		typeContext.setFirst(false);

		getContext().writeName(propertyName);
		getContext().writeQuoted(tipo.getStr());
		if (setContext) {
			getContext().writeCloseObject();
		}

	}

	@Override
	public Boolean isInline() {
		return Boolean.TRUE;
	}

}
