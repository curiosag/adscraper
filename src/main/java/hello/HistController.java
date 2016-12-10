package hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HistController {

	@RequestMapping("/hist")
	public HistList greeting(@RequestParam(value = "number", defaultValue = "0") String number) {		
		return new HistList(UtilTest.createItems(5));
	}
}