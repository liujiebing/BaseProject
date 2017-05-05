/**
 * 
 */
package com.platform.helper;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class EntryIntent extends android.content.Intent implements Serializable, Parcelable {

	public EntryIntent(String action){
		super(action);
	}
	
	public EntryIntent(){
		super();
	}

	public EntryIntent(Parcel in) {
		super.readFromParcel(in);
	}


	public static final Creator<EntryIntent> CREATOR=new Creator<EntryIntent>(){

		@Override
		public EntryIntent createFromParcel(Parcel source) {
			return new EntryIntent(source);
		}

		@Override
		public EntryIntent[] newArray(int size) {
			return new EntryIntent[size];
		}
	};

	public static final String ACTION_NET_ERROR = "XG.ACTION_NET_ERROR";


}
