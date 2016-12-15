package app.server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.cg.base.Const;
import org.cg.base.Log;
import org.cg.common.util.StringUtil;
import org.cg.dispatch.MailDelivery;
import org.cg.history.History;
import org.cg.hub.Scraper;
import org.cg.hub.Settings;
import org.cg.rendering.ObjectRenderer;
import org.cg.util.http.HttpUtil;
import org.springframework.stereotype.Controller;

import com.google.common.base.Throwables;

import app.storage.Repos;
import app.storage.RepositoryItem;

@SuppressWarnings("unused")
@Controller
public class CommandHandler {

	private String[] adminUserCommands = { "clip", "m", "set", "unset", "p", "x" };

	private String[] adminUsers = { "curiosa.globunznik@gmail.com" };

	private final List<RepositoryItem> repos;

	private Collector<RepositoryItem, ?, List<RepositoryItem>> coll() {
		return Collectors.toList();
	}

	private boolean isAdminUser() {
		return true;
	}

	public CommandHandler(Repos repos) {
		this.repos = repos.getItems();
	}

	private boolean isAdminCmd(String cmd) {
		for (String s : adminUserCommands)
			if (s.equals(cmd))
				return true;
		return false;
	}

	public String hdlCmd(String input) throws IllegalArgumentException {
		System.out.println(String.format("cmd: '%s'",  input));
		if (input == null || input.length() == 0 || input.equals(app.Const.NOCMD))
			return "that's not a command";
		
		String cmd = cmdToken(input);

		try {
			switch (cmd) {

			case "suspend": {
				return Settings.instance().set("switch:" + Const.SETTING_SWITCH_SUSPENDED + "=true");
			}

			case "resume": {
				Settings.instance().del(Const.SETTING_SWITCH_SUSPENDED);
				return "unsuspended";
			}

			case "migHist": {
				return "not implemented";
			}

			case "2ft": {
				return "not implemented";
			}

			case "clip":
				return clip(input);

			case "h":
			case "help":
				return helpText();

			case "k":
				return hdlKind();

			case "l":
				return "not implemented";

			case "m":
				return MailDelivery.testMail();

			case "f":
				return MailDelivery.testFormat();

			case "set":
				return Settings.instance().set(input.substring(4));

			case "unset": {
				Settings.instance().del(input.substring(6));
				return "ok";
			}

			case "p":
				return purge(input);

			case "stat":
				return getStatPage();

			case "s":
				return getHttp(input);

			case "t": {
				return "t";
			}
			case "v":
				return hdlView(input.substring(2));
			case "csv":
				return "not implemented";

			case "x": {
				new Scraper().execute();
				return "Executed scan";
			}

			default:
				return "Command not recognized: " + input;
			}

		} catch (Exception e) {
			Log.logException(e, Const.ADD_STACK_TRACE);
			return e.getClass().getName() + " " + e.getMessage() + " " + Throwables.getStackTraceAsString(e);
		}
	}

	private String hdlKind() {
		return StringUtil.toCsv(repos.stream().map(x -> x._class.getSimpleName()).collect(Collectors.toList()), "\n");
	}

	@SuppressWarnings("unchecked")
	private String hdlView(String kind) {
		List<RepositoryItem> c = repos.stream().filter(x -> x._class.getSimpleName().equals(kind))
				.collect(Collectors.toList());
		String result = "";
		if (c.size() > 0) {
			result = new ObjectRenderer().renderTable(getN(c.get(0).repo.findAll(), 25));
		}
		
		return result;
	}

	private<T> List<T> getN(Iterable<T> all, int i) {
		ArrayList<T> result = new ArrayList<T>();
		for (T t : all) {
			if (result.size() == i)
				break;
			result.add(t);
		}
		return result;
	}

	private String cmdToken(String cmdString) {
		return cmdString.split("\\s")[0];
	}

	private String getHttp(String input) {
		String[] tokens = input.split("\\s");

		if (tokens.length != 2)
			return "usage: get [url]";

		String result = HttpUtil.getHtmlInputString(tokens[1]);
		if (result != null)
			result = result.replace("<img", "<iiimg");
		return result;
	}

	public static String clip(String input) {
		String[] tokens = input.split("\\s");

		if (tokens.length != 3)
			return "usage: clip [urlId] [number]";
		try {
			int num = Integer.parseInt(tokens[2]);
			return History.instance().clip(tokens[1], num);
		} catch (Exception e) {
			return e.getClass().getName() + "\n" + e.getMessage();
		}

	}

	private static String purge(String input) {
		String[] tokens = input.split("\\s");

		if (tokens.length != 2)
			return "usage: purge [entityKind]";

		return "not implemented";
	}

	private String htmlBr(String text) {
		return text + "<br>";
	}

	private String htmlB(String text) {
		return "<b>" + text + "</b>";
	}

	private String htmlBlank(int number) {
		String result = "";
		for (int i = 0; i <= number; i++)
			result = result + "&nbsp;";
		return result;
	}

	private String helpText() {
		StringBuilder result = new StringBuilder();
		result.append(htmlB("ehm...")).append(htmlBr("")).append(htmlBr(""));
		result.append("'h' or 'help': this Text.").append(htmlBr("")).append(htmlBr(""));
		result.append(htmlBr(htmlBr("'stat': opens scraper statistics page in new tab")));
		result.append(htmlBr(htmlBr(
				"'clip [urlId] [n]' ... clips n items for urlId from history ring buffer (but not from permanent history)")));

		result.append(htmlBr("'set [settingtype]:[settingname]=[setting-Value]' ... define settings"))
				.append(htmlBr(""));
		result.append("unset [settingname]   ... delete it ").append(htmlBr("")).append(htmlBr(""));

		result.append(htmlBlank(4)).append("Example:").append(htmlBr("")).append(htmlBr(""));
		result.append(htmlBlank(4)).append("set url:bazar.at=http://www.bazar.at/wien-wohnungen-anzeigen,dir,1,cId")
				.append(htmlBr("")).append(htmlBr(""));

		result.append(htmlBr(htmlBr("'f': show html format of notification emails")));
		result.append(htmlBr(htmlBr("'k': list existing entity kinds")));
		result.append(htmlBr(htmlBr("'l': show last log lines")));

		result.append(htmlBr(htmlBr("'m': send test mail")));
		result.append(htmlBr(htmlBr("'p [entityKind]': purge all entities of kind entityKind")));

		result.append(htmlBr(htmlBr("'v [entityKind]': display entities of kind entityKind")));
		result.append(htmlBr(htmlBr("'csv [entityKind]': like above, but as csv")));

		result.append(htmlBr(htmlBr("'x': execute one round of scraping immediately")));

		result.append(htmlBr(htmlBr("'2ft': load detail ad history into fusion tables")));

		result.append(htmlBr(htmlBr("use up/down arrow keys to scroll in command history")));
		result.append(htmlBr(htmlBr("")));
		result.append(htmlBr(htmlBr("SWITCHES:")));
		result.append(htmlBr(htmlBr("misc:suspended  ... scan function terminates without scanning")));
		result.append(htmlBr(htmlBr("misc:ftRelay  ... pass on detail history to fusion tables")));
		result.append(htmlBr(htmlBr(
				"pred:dictionary  ... dictionary csv for ad status prediction. entries of form <frequency>,<term>; NULL for empty dict.")));
		result.append(htmlBr(htmlBr("pred:theta  ... theta csv for ad status prediction. entries separated by ';'")));
		result.append(htmlBr(
				htmlBr("pred:killers  ... killer terms indicating fraud for prediction. entries separated by ';'")));

		result.append(htmlBr(htmlBr("filter:filterId=filterTerm[,<filterTerm>]*")));
		result.append(htmlBr(htmlBr(
				"rule:ruleId=rule[;rule]*  where rule is an expression like: 150 <= prize <= 450 & size > 0 & passes(description, filterId)")));
		result.append(htmlBr(htmlBr("dispatchRule:ruleId=emailId;ruleId")));

		return result.toString();
	}

	private String getStatPage() {
		String q = "\"";

		String a = "<div id=" + q + "graphdiv" + q + "></div>" + "<script type=" + q + "text/javascript" + q + ">"
				+ "  g = new Dygraph(" + "    document.getElementById(" + q + "graphdiv" + q + ")," + q
				+ "Date,Temperature" + q + ");" + "</script>" + q;

		return "Nein!";

	}

}
