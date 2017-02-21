package com.researchspace.figshare.model;

import java.net.URL;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class License {
	
	URL url;
	String name;
	Integer value;
	boolean defaultLicense;

}
