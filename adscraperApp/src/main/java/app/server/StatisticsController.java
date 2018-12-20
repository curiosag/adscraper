package app.server;

import org.cg.hub.StatisticsRenderer;

public class StatisticsController {

	//@RequestMapping("/stat") @RequestParam(value = "f") @RequestParam(value = "i")
	public String greeting(String f,  String i) {
		return (new StatisticsRenderer()).createStatistics(f, i);
	}
}