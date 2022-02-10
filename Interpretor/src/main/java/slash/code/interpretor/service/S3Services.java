package slash.code.interpretor.service;

import org.springframework.context.annotation.Profile;

@Profile("AWS")
public class S3Services {
//    private Logger logger = LoggerFactory.getLogger(S3Services.class);

//    @Autowired
//    private AmazonS3 s3client;

//    @Value("${}")
//    private String bucketName;

//    @Override
//    public ByteArrayOutputStream downloadFile(String keyName) {
//        try {
//            S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, keyName));
//
//            InputStream is = s3object.getObjectContent();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            int len;
//            byte[] buffer = new byte[4096];
//            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
//                baos.write(buffer, 0, len);
//            }
//
//            return baos;
//        } catch (IOException ioe) {
//            logger.error("IOException: " + ioe.getMessage());
//        } catch (AmazonServiceException ase) {
//            logger.info("sCaught an AmazonServiceException from GET requests, rejected reasons:");
//            logger.info("Error Message:    " + ase.getMessage());
//            logger.info("HTTP Status Code: " + ase.getStatusCode());
//            logger.info("AWS Error Code:   " + ase.getErrorCode());
//            logger.info("Error Type:       " + ase.getErrorType());
//            logger.info("Request ID:       " + ase.getRequestId());
//            throw ase;
//        } catch (AmazonClientException ace) {
//            logger.info("Caught an AmazonClientException: ");
//            logger.info("Error Message: " + ace.getMessage());
//            throw ace;
//        }
//
//        return null;
//    }

//    @Override
//    public void uploadFile(String keyName, MultipartFile file) {
//        try {
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentLength(file.getSize());
//            s3client.putObject(bucketName, keyName, file.getInputStream(), metadata);
//        } catch(IOException ioe) {
//            logger.error("IOException: " + ioe.getMessage());
//        } catch (AmazonServiceException ase) {
//            logger.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
//            logger.info("Error Message:    " + ase.getMessage());
//            logger.info("HTTP Status Code: " + ase.getStatusCode());
//            logger.info("AWS Error Code:   " + ase.getErrorCode());
//            logger.info("Error Type:       " + ase.getErrorType());
//            logger.info("Request ID:       " + ase.getRequestId());
//            throw ase;
//        } catch (AmazonClientException ace) {
//            logger.info("Caught an AmazonClientException: ");
//            logger.info("Error Message: " + ace.getMessage());
//            throw ace;
//        }
//    }

//    public List listFiles() {
//
//        ListObjectsRequest listObjectsRequest =
//                new ListObjectsRequest()
//                        .withBucketName(bucketName);
//        //.withPrefix("test" + "/");
//
//        List keys = new ArrayList();
//
//        ObjectListing objects = s3client.listObjects(listObjectsRequest);
//        List<S3ObjectSummary> summaries = new ArrayList<>();
//      if (objects.getObjectSummaries().size()>0){
//          summaries.addAll(objects.getObjectSummaries());
//      }
//
//            if (summaries.size()>0){return summaries;}
//            else {return new ArrayList();}
//
//
//}


}