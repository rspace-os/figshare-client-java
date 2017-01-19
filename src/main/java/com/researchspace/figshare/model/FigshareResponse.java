package com.researchspace.figshare.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper class around Figshare responses that may fail because of invalid submitted client data, rather
 *  than an unexpected runtime error.
 *  <p>
 *  Either data or error can be set.
 *  </p>
 * @author rspace
 *
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FigshareResponse <T> {
	T data;
	FigshareError error;

	public boolean hasError (){
		return error != null;
	}
}
