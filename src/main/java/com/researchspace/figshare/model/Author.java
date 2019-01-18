package com.researchspace.figshare.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
	@JsonInclude(Include.NON_NULL)
	private Integer id;

}
