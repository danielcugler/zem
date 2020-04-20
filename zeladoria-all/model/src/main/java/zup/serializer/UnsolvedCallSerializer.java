package zup.serializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import zup.bean.Entity;
import zup.bean.EntityCategory;
import zup.bean.UnsolvedCall;

public class UnsolvedCallSerializer extends JsonSerializer<UnsolvedCall> {
	
    @Override
    public void serialize(UnsolvedCall value, JsonGenerator jgen, SerializerProvider provider) 
      throws IOException, JsonProcessingException {
    	 DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	jgen.writeStartObject();
        jgen.writeNumberField("unsolvedCallId", value.getUnsolvedCallId());
        if(value.getParentCallId()!=null){                    	  
        jgen.writeObjectFieldStart("parentCallId");
        jgen.writeNumberField("unsolvedCallId", value.getParentCallId().getUnsolvedCallId());
        jgen.writeStringField("creationOrUpdateDate", df.format(value.getParentCallId().getCreationOrUpdateDate()));
        jgen.writeEndObject();
        }
        if(value.getDescription()!=null){            
        jgen.writeObjectFieldStart("description");
        jgen.writeStringField("information", value.getDescription().getInformation());
        jgen.writeEndObject();
        }
        if(value.getObservation()!=null){
        jgen.writeObjectFieldStart("observation");
        jgen.writeStringField("information", value.getObservation().getInformation());
        jgen.writeEndObject();
        }
        if(value.getRemove()!=null){
        jgen.writeBooleanField("remove", value.getRemove());
        }
        if(value.getCallClassificationId()!=null){
        jgen.writeObjectFieldStart("callClassificationId");
        jgen.writeStringField("name", value.getCallClassificationId().getName());
        jgen.writeNumberField("callClassificationId", value.getCallClassificationId().getCallClassificationId());  
        jgen.writeBooleanField("addressRequired", value.getCallClassificationId().getAddressRequired());
        jgen.writeEndObject();
        }
        if(value.getUpdatedOrModeratedBy()!=null){
        jgen.writeObjectFieldStart("updatedOrModeratedBy");
        jgen.writeStringField("systemUserUsername", value.getUpdatedOrModeratedBy().getSystemUserUsername());
        jgen.writeEndObject();
        }        
        if(value.getAddressId()!=null){
        jgen.writeObjectFieldStart("addressId");
        jgen.writeStringField("streetName", value.getAddressId().getStreetName());
        if(value.getAddressId().getAddressNumber()!=null)
        jgen.writeNumberField("addressNumber", value.getAddressId().getAddressNumber());
        if(value.getAddressId().getComplement()!=null)
        jgen.writeStringField("complement", value.getAddressId().getComplement());
        jgen.writeObjectFieldStart("neighborhoodId");       
        jgen.writeStringField("name", value.getAddressId().getNeighborhoodId().getName());
        jgen.writeNumberField("neighborhoodId", value.getAddressId().getNeighborhoodId().getNeighborhoodId());
        jgen.writeEndObject();
        jgen.writeEndObject();
        }
        
 //       jgen.writeObjectField("entityEntityCategoryMaps", value.getEntityEntityCategoryMaps());  
         jgen.writeObjectFieldStart("entityEntityCategoryMaps");
        jgen.writeObjectFieldStart("entityCategory");
        jgen.writeNumberField("entityCategoryId", value.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK().getEntityCategoryId());
       if(value.getEntityEntityCategoryMaps().getEntityCategory()!=null){
        jgen.writeStringField("name", value.getEntityEntityCategoryMaps().getEntityCategory().getName());
        jgen.writeStringField("send_message", value.getEntityEntityCategoryMaps().getEntityCategory().getSend_message().getStr());        
       }
        jgen.writeEndObject();
        jgen.writeObjectFieldStart("entity");
        jgen.writeNumberField("entityId", value.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK().getEntityId());
        if(value.getEntityEntityCategoryMaps().getEntity()!=null)
        jgen.writeStringField("name", value.getEntityEntityCategoryMaps().getEntity().getName());
        jgen.writeEndObject();
        jgen.writeEndObject();
        jgen.writeStringField("callSource", value.getCallSource().getStr());
        jgen.writeStringField("callStatus", value.getCallStatus().getStr());
        jgen.writeStringField("callProgress", value.getCallProgress().getStr());
        jgen.writeStringField("priority", value.getPriority().getStr());
        jgen.writeStringField("creationOrUpdateDate", df.format(value.getCreationOrUpdateDate()));
        jgen.writeBooleanField("noMidia", value.isNoMidia());
        jgen.writeStringField("firstPhoto", value.getFirstPhoto());
        if(value.getQualification()!=null)
        jgen.writeNumberField("qualification", value.getQualification());  
        if(value.getDelay()!=null)
            jgen.writeStringField("delay", value.getDelay()); 
        if(value.getExpirationDate()!=null)
            jgen.writeStringField("expirationDate", df.format(value.getExpirationDate())); 
        if(value.getMediasPath()!=null)
        jgen.writeObjectField("mediasPath", value.getMediasPath());       
        jgen.writeEndObject();
    }
}

