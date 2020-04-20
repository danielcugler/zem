package zup.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidExpiresRangeException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.MinioException;
import io.minio.errors.NoResponseException;
import io.minio.messages.Item;

public class TesteMinio {

	public static void save()
			throws InvalidKeyException, NoSuchAlgorithmException, IOException, XmlPullParserException {
		try {
			MinioClient minioClient = new MinioClient("http://192.168.4.100:9000", "6BTP2IW1DFNTEAIBVPT2",
					"VamB9WGfXikQYn+QTB6NO13lszJX29vOHP48IPmk");
			File file = new File("C://Users/Labitec01//Pictures//1.png");
			FileInputStream fi = new FileInputStream(file);
			minioClient.putObject("call", "/1366/1.png", fi, file.length(), "image/png");
		} catch (MinioException e) {
			System.out.println("Error occurred: " + e);
		}
	}

	public static String getCallImage() throws InvalidKeyException, InvalidBucketNameException, NoSuchAlgorithmException, InsufficientDataException, NoResponseException, ErrorResponseException, InternalException, InvalidExpiresRangeException, InvalidEndpointException, InvalidPortException, IOException, XmlPullParserException{
		MinioServer ms=new MinioServer();
		String url= ms.getMinioClient().presignedGetObject("call", "/56/1.jpg");
	System.out.println(url);
	return url;
	}
	
	public static String getImage()
			throws InvalidKeyException, NoSuchAlgorithmException, IOException, XmlPullParserException, MinioException {
		MinioServer ms=new MinioServer();
		try {
	//		MinioClient minioClient = new MinioClient("http://192.168.4.100:9000", "6BTP2IW1DFNTEAIBVPT2",
		//			"VamB9WGfXikQYn+QTB6NO13lszJX29vOHP48IPmk");
			MinioClient minioClient = 	ms.getMinioClient();	
			Iterable<Result<Item>> myObjects = minioClient.listObjects("call", "/110/");
			if (myObjects.iterator().hasNext()) {
				String url = minioClient.presignedGetObject("user", "root.jpg", 10);
				System.out.println(url);
				return url;
			} else {
				System.out.println("erro");
				return null;
			}
		} catch (MinioException e) {
			System.out.println("Error occurred: " + e);
			throw e;
		}
	}
	
	
	public static List<String> getMedia(int id) throws InvalidKeyException, InvalidBucketNameException, NoSuchAlgorithmException, InsufficientDataException, NoResponseException, ErrorResponseException, InternalException, IOException, XmlPullParserException, InvalidEndpointException, InvalidPortException, InvalidExpiresRangeException{
		List<String> list= new ArrayList<String>();
		MinioServer minioServer= new MinioServer();
		MinioClient minioClient = minioServer.getMinioClient();
		Iterable<io.minio.Result<Item>> myObjects = minioClient.listObjects("call", "/" + id + "/");
		for (io.minio.Result<Item> result : myObjects) {
			Item item = result.get();
			String url = minioClient.presignedGetObject("call", item.objectName());
			list.add(url);

	}
		return list;
	}

	public static void makeBucket() throws InvalidEndpointException, InvalidPortException, InvalidKeyException,
			InvalidBucketNameException, NoSuchAlgorithmException, InsufficientDataException, NoResponseException,
			ErrorResponseException, InternalException, IOException, XmlPullParserException {
		MinioClient minioClient = new MinioClient("http://192.168.4.100:9000", "6BTP2IW1DFNTEAIBVPT2",
				"VamB9WGfXikQYn+QTB6NO13lszJX29vOHP48IPmk");
		// Make a new bucket called asiatrip to hold a zip file of photos.
		minioClient.makeBucket("1333");
	}

	public static void main(String[] args)
			throws InvalidKeyException, NoSuchAlgorithmException, IOException, XmlPullParserException, MinioException {
	
	//	for(String url:getMedia(1378))
	//		System.out.println(url);
		getCallImage();
		// makeBucket();
		//save();
	}
}
