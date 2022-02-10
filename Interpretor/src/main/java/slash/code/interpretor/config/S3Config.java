package slash.code.interpretor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("AWS")
@Configuration
public class S3Config {


//
//        @Value("${slash.aws.access_key_id}")
//        private String awsId;
//
//        @Value("${slash.aws.secret_access_key}")
//        private String awsKey;
//
//        @Value("${slash.s3.region}")
//        private String region;
//
//        @Bean
//        public AmazonS3 s3client() {
//
//            BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
//
//            return AmazonS3ClientBuilder.standard()
//                    .withRegion(Regions.fromName(region))
//                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
//                    .build();
//        }
}

