package gov.hhs.cms.bluebutton.datapipeline.rif.extract.s3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import gov.hhs.cms.bluebutton.datapipeline.rif.extract.exceptions.ChecksumException;
import gov.hhs.cms.bluebutton.datapipeline.rif.extract.s3.DataSetManifest.DataSetManifestEntry;
import gov.hhs.cms.bluebutton.datapipeline.rif.extract.s3.task.ManifestEntryDownloadTask;

/**
 * <p>
 * Contains utilities that are useful when running tests that involve working
 * with data sets in S3.
 * </p>
 * <p>
 * This is being left in <code>src/main</code> so that it can be used from other
 * modules' tests, without having to delve into classpath dark arts.
 * </p>
 */
public class DataSetTestUtilities {
	/**
	 * Deletes the specified {@link Bucket} and all objects in it.
	 * 
	 * @param s3Client
	 *            the {@link AmazonS3} client to use
	 * @param bucket
	 *            the {@link Bucket} to empty and delete
	 */
	public static void deleteObjectsAndBucket(AmazonS3 s3Client, Bucket bucket) {
		ListObjectsV2Request s3BucketListRequest = new ListObjectsV2Request();
		s3BucketListRequest.setBucketName(bucket.getName());
		ListObjectsV2Result s3ObjectListing;
		do {
			s3ObjectListing = s3Client.listObjectsV2(s3BucketListRequest);
			for (S3ObjectSummary objectSummary : s3ObjectListing.getObjectSummaries()) {
				s3Client.deleteObject(bucket.getName(), objectSummary.getKey());
			}

			s3BucketListRequest.setContinuationToken(s3ObjectListing.getNextContinuationToken());
		} while (s3ObjectListing.isTruncated());
		s3Client.deleteBucket(bucket.getName());
	}

	/**
	 * @param bucket
	 *            the {@link Bucket} to place the new object in
	 * @param manifest
	 *            the {@link DataSetManifest} to push as an object
	 * @return a {@link PutObjectRequest} for the specified
	 *         {@link DataSetManifest}
	 */
	public static PutObjectRequest createPutRequest(Bucket bucket, DataSetManifest manifest) {
		String keyPrefix = String.format("%s/%s", DataSetMonitorWorker.S3_PREFIX_PENDING_DATA_SETS,
				manifest.getTimestampText());
		return createPutRequest(bucket, keyPrefix, manifest);
	}

	/**
	 * @param bucket
	 *            the {@link Bucket} to place the new object in
	 * @param keyPrefix
	 *            the S3 key prefix to store the new object under
	 * @param manifest
	 *            the {@link DataSetManifest} to push as an object
	 * @return a {@link PutObjectRequest} for the specified
	 *         {@link DataSetManifest}
	 */
	public static PutObjectRequest createPutRequest(Bucket bucket, String keyPrefix, DataSetManifest manifest) {
		String objectKey = String.format("%s/%d_%s", keyPrefix, manifest.getSequenceId(), "manifest.xml");

		try {
			// Serialize the manifest to a byte array.
			JAXBContext jaxbContext = JAXBContext.newInstance(DataSetManifest.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			ByteArrayOutputStream manifestOutputStream = new ByteArrayOutputStream();
			marshaller.marshal(manifest, manifestOutputStream);

			byte[] manifestByteArray = manifestOutputStream.toByteArray();
			InputStream manifestInputStream = new ByteArrayInputStream(manifestByteArray);

			// If this isn't specified, the AWS API logs annoying warnings.
			ObjectMetadata manifestMetadata = new ObjectMetadata();
			manifestMetadata.setContentLength(manifestByteArray.length);

			PutObjectRequest request = new PutObjectRequest(bucket.getName(), objectKey, manifestInputStream,
					manifestMetadata);
			return request;
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param bucket
	 *            the {@link Bucket} to place the new object in
	 * @param manifest
	 *            the {@link DataSetManifest} to create an object for
	 * @param manifestEntry
	 *            the {@link DataSetManifestEntry} to create an object for
	 * @param objectContentsUrl
	 *            a {@link URL} to the data to push as the new object's content
	 * @return a {@link PutObjectRequest} for the specified content
	 */
	public static PutObjectRequest createPutRequest(Bucket bucket, DataSetManifest manifest,
			DataSetManifestEntry manifestEntry, URL objectContentsUrl) {
		String keyPrefix = String.format("%s/%s", DataSetMonitorWorker.S3_PREFIX_PENDING_DATA_SETS,
				manifest.getTimestampText());
		return createPutRequest(bucket, keyPrefix, manifest, manifestEntry, objectContentsUrl);
	}

	/**
	 * @param bucket
	 *            the {@link Bucket} to place the new object in
	 * @param keyPrefix
	 *            the S3 key prefix to store the new object under
	 * @param manifest
	 *            the {@link DataSetManifest} to create an object for
	 * @param manifestEntry
	 *            the {@link DataSetManifestEntry} to create an object for
	 * @param objectContentsUrl
	 *            a {@link URL} to the data to push as the new object's content
	 * @return a {@link PutObjectRequest} for the specified content
	 */
	public static PutObjectRequest createPutRequest(Bucket bucket, String keyPrefix, DataSetManifest manifest,
			DataSetManifestEntry manifestEntry, URL objectContentsUrl) {
		String objectKey = String.format("%s/%s", keyPrefix, manifestEntry.getName());

		try {
			// If this isn't specified, the AWS API logs annoying warnings.
			int objectContentLength = objectContentsUrl.openConnection().getContentLength();
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(objectContentLength);

			// create md5chksum on file to be uploaded
			objectMetadata.addUserMetadata("md5chksum",
					ManifestEntryDownloadTask.computeMD5ChkSum(objectContentsUrl.openStream()));

			PutObjectRequest request = new PutObjectRequest(bucket.getName(), objectKey, objectContentsUrl.openStream(),
					objectMetadata);

			/*
			 * Per https://github.com/aws/aws-sdk-java/issues/427, this is
			 * required when PUTing objects from an InputStream (as opposed to a
			 * File). Without it, was seeing intermittent errors.
			 */
			request.getRequestClientOptions().setReadLimit(objectContentLength + 1);

			return request;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new ChecksumException("NoSuchAlgorithmException on file " + manifest.getTimestampText()
					+ manifestEntry.getName() + "trying to build md5chksum", e);
		}

	}

	/**
	 * <p>
	 * Waits for the number of objects in the specified {@link Bucket} to equal
	 * the specified count.
	 * </p>
	 * <p>
	 * This is needed because Amazon's S3 API is only <em>eventually</em>
	 * consistent for deletes, per
	 * <a href="https://aws.amazon.com/s3/faqs/">Amazon S3 FAQs</a>.
	 * </p>
	 * 
	 * @param s3Client
	 *            the {@link AmazonS3} client to use
	 * @param bucket
	 *            the {@link Bucket} to check
	 * @param keyPrefix
	 *            the S3 object key prefix of the objects to include in the
	 *            count
	 * @param expectedObjectCount
	 *            the number of objects that should be in the specified
	 *            {@link Bucket}
	 * @param waitDuration
	 *            the length of time to wait for the condition to be met before
	 *            throwing an error
	 */
	public static void waitForBucketObjectCount(AmazonS3 s3Client, Bucket bucket, String keyPrefix,
			int expectedObjectCount, Duration waitDuration) {
		Instant endTime = Instant.now().plus(waitDuration);

		int actualObjectCount = -1;
		while (Instant.now().isBefore(endTime)) {
			actualObjectCount = s3Client.listObjects(bucket.getName(), String.format("%s/", keyPrefix))
					.getObjectSummaries().size();
			if (expectedObjectCount == actualObjectCount)
				return;

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// Shouldn't happen, as we're not using interrupts for anything.
				throw new IllegalStateException(e);
			}
		}

		throw new IllegalStateException(
				String.format("S3 object count count incorrect. Expected '%d', but actual is '%d'.",
						expectedObjectCount, actualObjectCount));
	}
}
