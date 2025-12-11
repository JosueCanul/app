package com.uady.awsfundations.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

@Configuration
public class DynamoConfig {

    @Value("${aws.dynamodb.endpoint}")
    private String dynamoEndpoint;
    
    @Value("${aws.dynamodb.region}")
    private String dynamoRegion;
    
    @Value("${aws.accessKeyId}")
    private String accessKey;
    
    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.sessionToken}")
    private String sessionToken;

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDB());
    }

    private AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder
        .standard()
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration(
                dynamoEndpoint, dynamoRegion)
        )
        .withCredentials(
            new AWSStaticCredentialsProvider(
                new BasicSessionCredentials(accessKey, secretKey, sessionToken)
            )
        )
        .build();
    }
    
}
