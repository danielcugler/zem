package zup.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import zup.enums.InformationType;

public class InformationTypeSerializer extends JsonSerializer<InformationType> {
	  @Override
	  public void serialize(InformationType value, JsonGenerator generator,
	            SerializerProvider provider) throws IOException,
	            JsonProcessingException {

	    generator.writeStartObject();
	    generator.writeFieldName("code");
	    generator.writeNumber(value.getValue());
	    generator.writeFieldName("name");
	    generator.writeString(value.getStr());
	    generator.writeEndObject();
	  }
	}