package zup.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import zup.bean.Entity;
import zup.bean.EntityCategory;

public class EntitySerializer extends JsonSerializer<Entity> {
    @Override
    public void serialize(Entity value, JsonGenerator jgen, SerializerProvider provider) 
      throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeNumberField("entityId", value.getEntityId());
        jgen.writeStringField("name", value.getName());
        jgen.writeStringField("enabled", value.getEnabled().toString());
        jgen.writeObjectField("attendanceTime", value.getAttendanceTime());
        jgen.writeStartArray();
        for (EntityCategory ec : value.getEntityCategoryCollection()) {
            jgen.writeStartObject();
            jgen.writeNumberField("entityCategoryId", ec.getEntityCategoryId());
            jgen.writeStringField("name", ec.getName());
            jgen.writeStringField("enabled", ec.getEnabled().toString());
            jgen.writeEndObject();    
        }
        jgen.writeEndArray();
        jgen.writeEndObject();
    }
}