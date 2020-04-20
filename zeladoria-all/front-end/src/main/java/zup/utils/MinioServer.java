package zup.utils;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

public final class MinioServer {
	public MinioClient getMinioClient() throws InvalidEndpointException, InvalidPortException {
	//server digital ocean
			return new MinioClient("http://www.souzem.com.br:9000", "3N9OPYQAWCG0SD737Z1T","X5NbDLUWdwJp0t8SnBbpyTnBHoT+Msr85RS2sKsC");
	//server if	
	//	return new MinioClient("www.souzem.com.br/desenvolvimento:9000", "6BTP2IW1DFNTEAIBVPT2","VamB9WGfXikQYn+QTB6NO13lszJX29vOHP48IPmk");
		//return new MinioClient("http://191.252.81.232:9000", "ASPTRAZNHZ2IU69LQ96R","oSZwVjAcg13PcImx6LARyuxnObIDHifSut1DZKSw");

	}
}
