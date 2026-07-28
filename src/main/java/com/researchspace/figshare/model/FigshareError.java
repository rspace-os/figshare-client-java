package com.researchspace.figshare.model;

import lombok.Data;
import org.springframework.http.HttpStatusCode;

@Data
public class FigshareError {
	String message, code;
	HttpStatusCode status;

}
