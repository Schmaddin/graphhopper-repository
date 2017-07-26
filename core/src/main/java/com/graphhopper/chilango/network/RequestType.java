package com.graphhopper.chilango.network;

import java.io.Serializable;

public enum RequestType implements Serializable {
	close(-1), whenLastUpdate(0), SubmitTask(50), SubmitFeedback(100), SubmitModeration(120), GetUserData(
			150), ChangeUserName(151), ChangeUserPosition(152), ChangeUserAvatar(
					153), RequestRouteExcludeList(201), RequestModerationTasks(200), PutLiveGPS(
							220), GetLiveGPS(221), TransactionConfirmation(500), SubmitError(-99), RequestTransactions(
									3333), RequestTransaction(3334), ChangeTransaction(3335), ConfirmTransaction(
											3336), DismissTransaction(3337), RequestRoutes(3338), RequestRoute(
													3339), ChangeRoute(3340), RequestUser(3341), ChangeUser(3342);

	private final int value;

	RequestType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
