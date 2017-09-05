package com.graphhopper.chilango.network;

import java.io.Serializable;

public enum RequestType implements Serializable {
	no(-9999), close(-1), whenLastUpdate(0), SubmitTask(50), SubmitFeedback(100), SubmitModeration(120), GetUserData(
			150), ChangeUserName(151), ChangeUserPosition(152), ChangeUserAvatar(153), RequestRouteExcludeList(
					201), RequestModerationTasks(200), PutLiveGPS(220), GetLiveGPS(221), TransactionConfirmation(
							500), SubmitError(-99), RequestTransactions(3333), RequestTransaction(
									3334), ChangeTransaction(3335), ConfirmTransaction(3336), DismissTransaction(
											3337), RequestRoutes(3338), RequestRoute(3339), ChangeRoute(
													3340), RequestUser(3341), ChangeUser(3342), RequestFeedback(
															3345), RequestFeedbacks(3346), ChangeFeedback(
																	3347), CreateHighscore(3348), DeployRoutes(
																			3349), CreateMapBoard(
																					3350), AddValueToMapBoard(
																							3351), DeployMapBoard(3352), AcceptSubmit(3353), AcceptChange(3354), DeployStatistics(3355), CleanDoubleEntries(3356), CalculateTrustForFeedback(3357);

	private final int value;

	RequestType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static RequestType fromValue(int value) {
		switch (value) {
		case -1:
			return close;
		case 0:
			return whenLastUpdate;
		case 50:
			return SubmitTask;
		case 100:
			return SubmitFeedback;
		case 120:
			return SubmitModeration;
		case 150:
			return GetUserData;
		case 151:
			return ChangeUserName;
		case 152:
			return ChangeUserPosition;
		case 153:
			return ChangeUserAvatar;
		case 200:
			return RequestModerationTasks;
		case 201:
			return RequestRouteExcludeList;
		case 220:
			return PutLiveGPS;
		case 221:
			return GetLiveGPS;
		case -99:
			return SubmitError;
		case 3333:
			return RequestTransactions;
		case 3334:
			return RequestTransaction;
		case 3335:
			return ChangeTransaction;
		case 3336:
			return ConfirmTransaction;
		case 3337:
			return DismissTransaction;
		case 3338:
			return RequestRoutes;
		case 3339:
			return RequestRoute;

		case 3340:
			return ChangeRoute;
		case 3341:
			return RequestUser;
		case 3342:
			return ChangeUser;
		case 3345:
			return RequestFeedback;
		case 3346:
			return RequestFeedbacks;
		case 3347:
			return ChangeFeedback;
		case 3348:
			return CreateHighscore;
		case 3349:
			return DeployRoutes;
		case 3350:
			return CreateMapBoard;
		case 3351:
			return AddValueToMapBoard;
		case 3352:
			return DeployMapBoard;
		case 3353:
			return AcceptSubmit;
		case 3354:
			return AcceptChange;
		case 3355:
			return DeployStatistics;
		case 3356:
			return CleanDoubleEntries;
		case 3357:
			return CalculateTrustForFeedback;
			
		default:
			return no;
		}

	}
}
