package gov.hhs.cms.bluebutton.datapipeline.rif.extract.s3;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import gov.hhs.cms.bluebutton.datapipeline.rif.model.RifFile;

/**
 * This {@link RifFile} implementation can be used for files that are backed by
 * {@link S3Object}s. Note that this lazy-loads the files, to ensure that
 * connections are not opened until needed.
 */
public final class S3RifFile implements RifFile {
	private final AmazonS3 s3Client;
	private final GetObjectRequest objectRequest;

	/**
	 * Constructs a new {@link S3RifFile} instance.
	 * 
	 * @param s3Client
	 *            the {@link AmazonS3} client to use to get the contents of the
	 *            object that backs this {@link S3RifFile}
	 * @param objectRequest
	 *            an S3 {@link GetObjectRequest} that will retrieve the object
	 *            represented by this {@link S3RifFile}
	 */
	public S3RifFile(AmazonS3 s3Client, GetObjectRequest objectRequest) {
		this.s3Client = s3Client;
		this.objectRequest = objectRequest;
	}

	/**
	 * @see gov.hhs.cms.bluebutton.datapipeline.rif.model.RifFile#getKey()
	 */
	@Override
	public String getKey() {
		// FIXME This op should be removed from the interface as it's not used.
		throw new UnsupportedOperationException();
	}

	/**
	 * @see gov.hhs.cms.bluebutton.datapipeline.rif.model.RifFile#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return String.format("%s:%s", objectRequest.getBucketName(), objectRequest.getKey());
	}

	/**
	 * @see gov.hhs.cms.bluebutton.datapipeline.rif.model.RifFile#getLastModifiedTimestamp()
	 */
	@Override
	public Instant getLastModifiedTimestamp() {
		// FIXME This op should be removed from the interface as it's not used.
		throw new UnsupportedOperationException();
	}

	/**
	 * @see gov.hhs.cms.bluebutton.datapipeline.rif.model.RifFile#getCharset()
	 */
	@Override
	public Charset getCharset() {
		return StandardCharsets.UTF_8;
	}

	/**
	 * @see gov.hhs.cms.bluebutton.datapipeline.rif.model.RifFile#open()
	 */
	@Override
	public InputStream open() {
		return s3Client.getObject(objectRequest).getObjectContent();
	}

	/**
	 * @see gov.hhs.cms.bluebutton.datapipeline.rif.model.RifFile#delete()
	 */
	@Override
	public void delete() {
		/*
		 * FIXME This op should be removed from the interface; wrong place for
		 * it.
		 */
		throw new UnsupportedOperationException();
	}
}