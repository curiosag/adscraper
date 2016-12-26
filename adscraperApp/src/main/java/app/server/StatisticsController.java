package app.server;

import org.cg.hub.StatisticsRenderer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

	@RequestMapping("/stat")
	public String greeting(@RequestParam(value = "f") String f, @RequestParam(value = "i") String i) {
		return (new StatisticsRenderer()).createStatistics(f, i);
	}
}