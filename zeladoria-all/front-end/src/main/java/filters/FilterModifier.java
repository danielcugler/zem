package filters;

import java.io.IOException;

import javax.ws.rs.core.MultivaluedMap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.jaxrs.cfg.EndpointConfigBase;
import com.fasterxml.jackson.jaxrs.cfg.ObjectWriterModifier;
/*
public class FilterModifier extends ObjectWriterModifier {
	private final FilterProvider provider;

	public FilterModifier(FilterProvider provider) {
		this.provider = provider;
	}

	@Override
	public ObjectWriter modify(EndpointConfigBase<?> endpoint, MultivaluedMap<String, Object> responseHeaders,
			Object valueToWrite, ObjectWriter w, JsonGenerator g) throws IOException {
		return w.with(provider);
	}

}
*/