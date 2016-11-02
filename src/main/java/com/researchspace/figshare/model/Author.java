package com.researchspace.figshare.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Either name or id can be set.
 * @author rspace
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {
	
	private String name;
	private Integer id;

}
