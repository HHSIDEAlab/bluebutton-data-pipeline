package gov.hhs.cms.bluebutton.datapipeline.rif.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Models rows from <code>PDE</code> RIF Files.
 */
public class PartDEventRow {
	public static int COMPOUND_CODE_NOT_COMPOUNDED = 1;
	public static int COMPOUND_CODE_COMPOUNDED = 2;
	/**
	 * FIXME is this the schema version or the record version?
	 * 
	 * @see Column#VERSION
	 */
	public int version;

	/**
	 * Indicates whether the record for the row is an insert, update, or delete.
	 * 
	 * @see Column#DML_IND
	 */
	public RecordAction recordAction;

	/**
	 * The CCW's assigned ID for this Part D Event.
	 * 
	 * @see Column#PDE_ID
	 */
	public String partDEventId;

	/**
	 * The CCW's assigned ID for the beneficiary this Part D Event is linked to.
	 * 
	 * @see Column#BENE_ID
	 */
	public String beneficiaryId;

	/**
	 * The date that the prescription was filled.
	 * 
	 * @see Column#SRVC_DT
	 */
	public LocalDate prescriptionFillDate;

	/**
	 * The date that the plan paid the pharmacy for the prescription.
	 * 
	 * @see Column#PD_DT
	 */
	public Optional<LocalDate> paymentDate;

	/**
	 * The two-letter code that represents the type of pharmacy provider
	 * identifier that is used in the {@link #serviceProviderId} field.
	 * 
	 * @see Column#SRVC_PRVDR_ID_QLFYR_CD
	 */
	public Optional<String> serviceProviderIdQualiferCode;

	/**
	 * The ID of the pharmacy or physicians' office that dispensed the drug, the
	 * type of which is described by the {@link #serviceProviderIdQualiferCode}
	 * field.
	 * 
	 * @see Column#SRVC_PRVDR_ID
	 */
	public String serviceProviderId;

	/**
	 * The two-letter code that represents the type of prescribe identifier that
	 * is used in the {@link #prescriberId} field.
	 * 
	 * @see Column#PRSCRBR_ID_QLFYR_CD
	 */
	public Optional<String> prescriberIdQualifierCode;

	/**
	 * The ID of the practitioner that prescribed the drug, the type of which is
	 * described by the {@link #prescriberIdQualifierCode} field.
	 * 
	 * @see Column#PRSCRBR_ID
	 */
	public String prescriberId;

	/**
	 * The prescription reference number assigned by the pharmacy at the time
	 * the prescription is filled.
	 * 
	 * @see Column#RX_SRVC_RFRNC_NUM
	 */
	public Long prescriptionReferenceNumber;

	/**
	 * The 11-digit code representing the drug using the National Drug Code
	 * (NDC).
	 * 
	 * @see Column#PROD_SRVC_ID
	 */
	public String nationalDrugCode;

	/**
	 * The ID representing the latest contract assigned to the beneficiary at
	 * the time of payment reconciliation. Note the first letter of the ID is a
	 * letter representing the type of plan.
	 * 
	 * @see Column#PLAN_CNTRCT_REC_ID
	 */
	public String planContractId;

	/**
	 * The ID representing the latest benefit package the beneficiary was
	 * enrolled in for the year.
	 * 
	 * @see Column#PLAN_PBP_REC_NUM
	 */
	public String planBenefitPackageId;

	/**
	 * Two-digit code representing whether the dispensed drug was compounded or
	 * mixed.
	 * 
	 * @see Column#CMPND_CD
	 */
	public Optional<Integer> compoundCode;

	/**
	 * One-digit code representing the prescriber's instructions regarding
	 * substitution of generic equivalents or the order to dispense the specific
	 * prescribed medication.
	 * 
	 * @see Column#DAW_PROD_SLCTN_CD
	 */
	public Optional<String> dispenseAsWrittenProductSelectionCode;

	/**
	 * The number of units, grams, milligrams, or other dispensed in the current
	 * drug event. For compounded items, the quantity dispensed is the total of
	 * all ingredients. (Note: After some research, it does not appear there is
	 * another field that specifies what unit this is.)
	 * 
	 * @see Column#QTY_DPSNSD_NUM
	 */
	public BigDecimal quantityDispensed;

	/**
	 * The number of days' supply of medication dispensed, entered by the
	 * pharmacy.
	 * 
	 * @see Column#DAYS_SUPLY_NUM
	 */
	public Optional<Integer> daysSupply;

	/**
	 * The number fill of the currently dispensed supply, with 0 being the
	 * original fill and 1-99 representing each refill.
	 * 
	 * @see Column#FILL_NUM
	 */
	public Integer fillNumber;

	/**
	 * One-digit code representing how the pharmacy dispensed the complete
	 * quantity of the prescription.
	 * 
	 * @see Column#DSPNSNG_STUS_CD
	 */
	public Optional<Character> dispensingStatuscode;

	// TODO FIll in rest of fields

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PartDEventRow [version=");
		builder.append(version);
		builder.append(", recordAction=");
		builder.append(recordAction);
		builder.append(", partDEventId=");
		builder.append(partDEventId);
		builder.append(", beneficiaryId=");
		builder.append(beneficiaryId);
		builder.append(", prescriptionFillDate=");
		builder.append(prescriptionFillDate);
		builder.append(", paymentDate=");
		builder.append(paymentDate);
		builder.append(", serviceProviderIdQualiferCode=");
		builder.append(serviceProviderIdQualiferCode);
		builder.append(", serviceProviderId=");
		builder.append(serviceProviderId);
		builder.append(", prescriberIdQualifierCode=");
		builder.append(prescriberIdQualifierCode);
		builder.append(", prescriberId=");
		builder.append(prescriberId);
		builder.append(", prescriptionReferenceNumber=");
		builder.append(prescriptionReferenceNumber);
		builder.append(", nationalDrugCode=");
		builder.append(nationalDrugCode);
		builder.append(", planContractId=");
		builder.append(planContractId);
		builder.append(", planBenefitPackageId=");
		builder.append(planBenefitPackageId);
		builder.append(", compoundCode=");
		builder.append(compoundCode);
		builder.append(", dispenseAsWrittenProductSelectionCode=");
		builder.append(dispenseAsWrittenProductSelectionCode);
		builder.append(", quantityDispensed=");
		builder.append(quantityDispensed);
		builder.append(", daysSupply=");
		builder.append(daysSupply);
		builder.append(", fillNumber=");
		builder.append(fillNumber);
		builder.append(", dispensingStatuscode=");
		builder.append(dispensingStatuscode);
		builder.append("]");
		return builder.toString();
		// TODO FIll in rest of fields
	}

	/**
	 * Enumerates the raw RIF columns represented in {@link PartDEventRow},
	 * where {@link Column#ordinal()} values represent each column's position in
	 * the actual data.
	 */
	public static enum Column {
		/**
		 * Type: (unknown), max chars: (unknown).
		 */
		VERSION,

		/**
		 * Type: (unknown), max chars: (unknown).
		 */
		DML_IND,

		/**
		 * Type: <code>CHAR</code>, max chars: 15
		 */
		PDE_ID,

		/**
		 * Type: <code>CHAR</code>, max chars: 15
		 */
		BENE_ID,

		/**
		 * Type: <code>DATE</code>, max chars: 8
		 */
		SRVC_DT,

		/**
		 * Type: <code>DATE</code>, max chars: 8, <code>NULLABLE</code>
		 */
		PD_DT,

		/**
		 * Type: <code>CHAR</code>, max chars: 2, <code>NULLABLE</code>
		 * 
		 * @see <a href=
		 *      "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/srvc_prvdr_id_qlfyr_cd.txt">
		 *      Data Dictionary</a>
		 */
		SRVC_PRVDR_ID_QLFYR_CD,

		/**
		 * Type: <code>CHAR</code>, max chars: 15
		 */
		SRVC_PRVDR_ID,

		/**
		 * Type: <code>CHAR</code>, max chars: 2, <code>NULLABLE</code>
		 * 
		 * @see <a href=
		 *      "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/prscrbr_id_qlfyr_cd.txt">
		 *      Data Dictionary</a>
		 */
		PRSCRBR_ID_QLFYR_CD,

		/**
		 * Type: <code>CHAR</code>, max chars: 15
		 */
		PRSCRBR_ID,

		/**
		 * Type: <code>NUM</code>, max chars: 12
		 */
		RX_SRVC_RFRNC_NUM,

		/**
		 * Type: <code>CHAR</code>, max chars: 19
		 * 
		 * TODO Link to official NDC directory?
		 */
		PROD_SRVC_ID,

		/**
		 * Type: <code>CHAR</code>, max chars: 5
		 * 
		 * @see <a href=
		 *      "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/plan_cntrct_rec_id.txt">
		 *      Data Dictionary</a>
		 */
		PLAN_CNTRCT_REC_ID,

		/**
		 * Type: <code>CHAR</code>, max chars: 3
		 */
		PLAN_PBP_REC_NUM,

		/**
		 * Type: <code>NUM</code>, max chars: 2, <code>NULLABLE</code>
		 * 
		 * @see <a href=
		 *      "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/cmpnd_cd.txt">
		 *      Data Dictionary</a>
		 */
		CMPND_CD,

		/**
		 * Type: <code>CHAR</code>, max chars: 1, <code>NULLABLE</code>
		 * 
		 * @see <a href=
		 *      "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/daw_prod_slctn_cd.txt">
		 *      Data Dictionary</a>
		 */
		DAW_PROD_SLCTN_CD,

		/**
		 * Type: <code>NUM</code>, max chars: 12
		 */
		QTY_DPSNSD_NUM,

		/**
		 * Type: <code>NUM</code>, max chars: 3, <code>NULLABLE</code>
		 */
		DAYS_SUPLY_NUM,

		/**
		 * Type: <code>NUM</code>, max chars: 3
		 */
		FILL_NUM,

		/**
		 * Type: <code>CHAR</code>, max chars: 1, <code>NULLABLE</code>
		 * 
		 * @see <a href=
		 *      "https://www.ccwdata.org/cs/groups/public/documents/datadictionary/dspnsng_stus_cd.txt">
		 *      Data Dictionary</a>
		 */
		DSPNSNG_STUS_CD
		// TODO FIll in rest of fields
	}
}