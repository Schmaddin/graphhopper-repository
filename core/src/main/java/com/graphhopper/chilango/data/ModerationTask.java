package com.graphhopper.chilango.data;

import java.io.Serializable;

import com.graphhopper.chilango.data.database.SubmitType;
import com.graphhopper.chilango.data.database.SubmitTypeInterface;
import com.graphhopper.chilango.network.Constants;

public class ModerationTask extends SubmitTypeInterface implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ModerationTask(SubmitTypeInterface moderationTaskUnit,
			long toModerateTransactionId,byte trustLevel) {
		super();
		this.moderationTaskUnit = moderationTaskUnit;
		this.moderationDecision = Constants.TRANSACTION_PENDING;
		this.toModerateTransactionId = toModerateTransactionId;
		this.transaction = -1L;
		this.trustLevel = trustLevel;
	}
	
	public ModerationTask(ModerationTask task,int moderationDecision,SubmitTypeInterface change)
	{
		this.moderationTaskUnit = change;
		this.moderationDecision = moderationDecision;
		this.toModerateTransactionId = task.getToModerateTransactionId();
		this.transaction = -1L;
		this.trustLevel = task.getTrustLevel();
	}
	
	public ModerationTask(ModerationTask task,long transaction)
	{
		this.moderationTaskUnit = task.getModerationTaskUnit();
		this.moderationDecision = task.getModerationDecision();
		this.toModerateTransactionId = task.getToModerateTransactionId();
		this.transaction = transaction;
		this.trustLevel = task.getTrustLevel();
	}



	public SubmitTypeInterface getModerationTaskUnit() {
		return moderationTaskUnit;
	}

	public int getModerationDecision() {
		return moderationDecision;
	}

	public long getToModerateTransactionId() {
		return toModerateTransactionId;
	}

	public long getTransaction() {
		return transaction;
	}

	@Override
	public SubmitType getType() {
		return SubmitType.getByValue(submitType);
	}

	private final int submitType = SubmitType.moderation.getValue();
	private final SubmitTypeInterface moderationTaskUnit;
	private final int moderationDecision;
	private final long toModerateTransactionId;
	private final long transaction;
	private final byte trustLevel;


	public byte getTrustLevel() {
		return trustLevel;
	}
	
	
	


}
