package com.bnrdo.uditmsgr.domain;

import com.bnrdo.uditmsgr.util.Constants.UpdateType;

public class TerminateUpdate extends Update{

	@Override
	public UpdateType getUpdateType() {
		return UpdateType.TERMINATE;
	}

}
