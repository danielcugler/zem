package JsonTransformers;

import flexjson.TypeContext;
import flexjson.transformer.AbstractTransformer;

public class FieldValueTransformer extends AbstractTransformer {
	private String transformedFieldName;

	public FieldValueTransformer(String transformedFieldName) {
		this.transformedFieldName = transformedFieldName;
	}

	public void transform(Object object) {
		boolean setContext = false;

		TypeContext typeContext = getContext().peekTypeContext();

		if (!typeContext.isFirst())
			getContext().writeComma();

		typeContext.setFirst(false);

		getContext().writeName(getTransformedFieldName());
		if (object.toString().equals("ENABLED"))
			getContext().writeQuoted("Ativo");
		else
			getContext().writeQuoted("Inativo");

		if (setContext) {
			getContext().writeCloseObject();
		}
	}

	@Override
	public Boolean isInline() {
		return Boolean.TRUE;
	}

	public String getTransformedFieldName() {
		return this.transformedFieldName;
	}
}
