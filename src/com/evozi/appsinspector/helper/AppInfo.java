/*******************************************************************************
 * Copyright (c) 2011 Evozi (xDragonZ) ; email@evozi.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.evozi.appsinspector.helper;

import android.graphics.drawable.Drawable;

public class AppInfo {
	public String appName = "";
	public String packageName = "";
	public String versionName = "";
	public String adPackagePrefix = "";
	public int versionCode = 0;
	public Drawable appIcon = null;

	public void print() {
		// Log.v("AppInfo","Name:"+appName+" Package:"+packageName);
		// Log.v("AppInfo","Name:"+appName+" versionName:"+versionName);
		// Log.v("AppInfo","Name:"+appName+" versionCode:"+versionCode);
	}

}
