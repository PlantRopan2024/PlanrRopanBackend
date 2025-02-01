package com.plants.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Service
public class S3Service {
	
	private final AmazonS3 s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public S3Service(@Value("${aws.accessKey}") String accessKey,@Value("${aws.secretKey}") String secretKey,@Value("${aws.region}") String region) 
    {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public String uploadFile(MultipartFile file ,String fileName) throws IOException {
        //String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
        //return s3Client.getUrl(bucketName, fileName).toString();  // Return public URL
        return this.preSignedUrl(fileName);
    }
    
    public String preSignedUrl(String fileName) {
    	Date expirationDate = new Date();
    	long time = expirationDate.getTime();
    	int hours = 2;
    	time = time + hours * 60 * 60 *1000;
    	expirationDate.setTime(time);
    	GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
    			.withMethod(HttpMethod.GET)
    			.withExpiration(expirationDate);
    	java.net.URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
    	return url.toString();
    }
    
    public List<String> allFiles(){
    	ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request().withBucketName(bucketName);
    	ListObjectsV2Result listObjectsV2Result = s3Client.listObjectsV2(listObjectsRequest);
    	List<S3ObjectSummary> objectSummaries = listObjectsV2Result.getObjectSummaries();
    	List<String> listFilesUrls = objectSummaries.stream().map(item -> this.preSignedUrl(item.getKey())).collect(Collectors.toList());
    	return listFilesUrls;
    }
    
    public String getFileNameImage(String fileName) {
    	S3Object object = this.s3Client.getObject(bucketName , fileName);
    	String url = preSignedUrl(object.getKey());
    	return url;
    }

}
