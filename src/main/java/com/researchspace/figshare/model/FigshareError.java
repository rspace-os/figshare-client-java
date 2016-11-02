package com.researchspace.figshare.model;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class FigshareError {
	String message, code;
	HttpStatus status;

}
