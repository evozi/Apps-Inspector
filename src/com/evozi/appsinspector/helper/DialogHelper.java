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

import android.app.Activity;
import android.widget.Toast;

public class DialogHelper {
	
	private static int backpress = 0;

	/**
	 * Exit
	 */
	public static void showExitDialog(final Activity activity) {
		backpress = (backpress + 1);
		
	    if (backpress <= 1) {
	       Toast.makeText(activity, " Press back again to exit ", Toast.LENGTH_SHORT).show();
	    } 
	    
	    else if (backpress > 1) {
	       activity.finish();
	       backpress = 0;
	    }
	    
	}
}