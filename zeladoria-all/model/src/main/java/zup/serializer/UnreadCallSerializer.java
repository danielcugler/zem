package zup.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import zup.bean.Entity;
import zup.bean.EntityCategory;
import zup.bean.UnreadCall;

public class UnreadCallSerializer extends JsonSerializer<UnreadCall> {
    @Override
    public void serialize(UnreadCall value, JsonGenerator jgen, SerializerProvider provider) 
      throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeNumberField("unreadCallId", value.getUnreadCallId());
        if(value.getUnsolvedCallId()!=null)
        jgen.writeNumberField("unsolvedCallId", value.getUnsolvedCallId().getUnsolvedCallId());
        if(value.getSolvedCallId()!=null)
        jgen.writeNumberField("solvedCallId", value.getSolvedCallId().getSolvedCallId());
        jgen.writeBooleanField("read", value.getRead());
        jgen.writeEndObject();
    }
}