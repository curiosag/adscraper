package org.cg.ads.integration;

import org.springframework.stereotype.Component;

@Component("starter")
public class Starter {
	public String trigger(){
		return "1";
	}
}
