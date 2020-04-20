package zup.webservices.adm;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import JsonTransformers.FieldNameTransformer;
import JsonTransformers.FieldValueTransformer;
import flexjson.JSONSerializer;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidExpiresRangeException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.NoResponseException;
import zup.bean.MessageModel;
import zup.enums.Enabled;

public class TesteMessageModel {
public static void main(String[] args) throws InvalidEndpointException, InvalidPortException, InvalidKeyException, InvalidBucketNameException, NoSuchAlgorithmException, InsufficientDataException, NoResponseException, ErrorResponseException, InternalException, InvalidExpiresRangeException, IOException, XmlPullParserException {
    MinioClient minioClient = new MinioClient("http://192.168.1.111:9000", "MG4DLUQQWUVM3510U4UU", "j+MSbJhC3kg+l7FH1jMhboXoe4MiZOepBKdsZ4c4");
/*
     // Check if the bucket already exists.
     boolean isExist = minioClient.bucketExists("userphoto");
     if(isExist) {
       System.out.println("Bucket already exists.");
     } else {
       // Make a new bucket called asiatrip to hold a zip file of photos.
       minioClient.makeBucket("userphoto");
     }
     File file = new File("C://Users/Labitec01//Pictures//pic.jpg");
     FileInputStream fi = new FileInputStream(file);
     // Upload the zip file to the bucket with putObject
     minioClient.putObject("userphoto", "user01.jpg", fi, file.length(), "image/jpeg");
     System.out.println("user01.jpg is successfully uploaded as user01.jpg in user.");
   } catch(MinioException e) {
     System.out.println("Error occurred: " + e);
   } 
   */
     String url = minioClient.presignedGetObject("user", "root.jpg", 60 * 60 * 24);	
     System.out.println(url);
}
}
