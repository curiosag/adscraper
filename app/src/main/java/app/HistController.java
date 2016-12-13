package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.data.HistList;
import app.data.UtilTest;

@RestController
public class HistController {

	@Autowired
	private HistRepository repository;

	@RequestMapping("/hist")
	public HistList greeting(@RequestParam(value = "number", defaultValue = "10") String number) {
		repository.save(UtilTest.createItems(5));
		return new HistList(repository.findAll());
	}
}