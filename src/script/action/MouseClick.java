package script.action;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;

import clients.Client;
import clients.SeleniumClient;

/**
 * Action that executes a mouse left click at the current location
 * @author Allen
 *
 */
public class MouseClick extends Action {
	
	private String css = "";
	
	public MouseClick(String raw) {
		super(raw);
		//format is click [css]
		//extracts css as w/e is after the space
		int space = raw.indexOf(' ');
		if(space>0) {
			css = raw.substring(space+1);
		}
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

}
