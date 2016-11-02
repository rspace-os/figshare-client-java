package com.researchspace.figshare.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilePart {
	private String  status;
	private Long startOffset, endOffset;
	private boolean locked;
	private Integer partNo;
}
