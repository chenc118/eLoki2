package script.action.impl;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;

import clients.Client;
import clients.SeleniumClient;
import script.action.ActionImpl;
import script.action.ActionCompatibility;
import script.action.Action;
import script.action.ActionArgParser;
import script.action.ActionTick;

/**
 * Action that executes a mouse left click at the current location
 * @author Allen
 *
 */
public class MouseClick extends ActionImpl {
	
	private int x;
	private int y;
	private String css = "";
	
	public MouseClick(String raw) {
		super(raw);
		//format is click [x] [y] [css]
		ActionArgParser ap = new ActionArgParser(raw);
		this.x = ap.getArgAsIntO(0).orElse(-1);
		this.y = ap.getArgAsIntO(1).orElse(-1);
		this.css = ap.getArgO(2).orElse("");
	}
	public MouseClick(Action original) {
		this(original.getRaw());
		this.tick = new ActionTick(original.getTick().getValue(), original.getTick().getResponse());
	}

	@Override
	public Action execute(Client client) {
		if (client instanceof SeleniumClient) {
			SeleniumClient sClient = (SeleniumClient) client;
			boolean clicked = false;
			if(!css.equals("")) {
				try {
					//click using the css selector
					sClient.getWebDriver().findElement(By.cssSelector(css)).click();
					clicked = true;
					//if there is an error with the selector etc
				}catch(Exception e) {
					clicked = false;
				}
			}
			//default path, just clicks the mouse at the curren tpoint
			if(!clicked) {
				Actions action = new Actions(sClient.getWebDriver());
				if(x>=0&&y>=0) {
					action.tick(sClient.getPointerInput().createPointerMove(Duration.ofMillis(0),
					PointerInput.Origin.viewport(), x, y));
				}
				action.tick(sClient.getPointerInput().createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
				action.tick(sClient.getPointerInput().createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
				action.perform();
			}
		}
			
		return super.next;
	}

	@Override
	public ActionCompatibility checkComptability(Client client) {
		if (client instanceof SeleniumClient) {
			return ActionCompatibility.Ok;
		}
		return ActionCompatibility.Incompatible;
	}
	
	@Override
	protected ActionTick.Response actionTickResponse() {
		return ActionTick.Response.UseTick;
	}
	@Override
	public Action clone() {
		return new MouseClick(this);
	}

}
