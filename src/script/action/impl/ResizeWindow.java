package script.action.impl;

import org.openqa.selenium.Dimension;

import clients.Client;
import clients.SeleniumClient;
import script.action.ActionImpl;
import script.action.ActionCompatibility;
import script.action.Action;
import script.action.ActionTick;

/**
 * Resizes the window to the given width and height
 * @author Allen
 *
 */
public class ResizeWindow extends ActionImpl {
	private int width;
	private int height;
	public ResizeWindow(String raw) {
		super(raw);
		String[] args = raw.split(" ");
		width = Integer.parseInt(args[1]);
		height = Integer.parseInt(args[2]);
	}
	public ResizeWindow(Action original) {
		this(original.getRaw());
		this.tick = new ActionTick(original.getTick().getValue(), original.getTick().getResponse());
	}

	@Override
	public Action execute(Client client) {
		if (client instanceof SeleniumClient) {
			SeleniumClient sClient = (SeleniumClient) client;
			//compute the size of borders/header with inner/outer Width/Height
			String[] dimensions = ((String)sClient.getJSExecutor()
			.executeScript("return `${window.innerWidth},${window.innerHeight},${window.outerWidth},${window.outerHeight}`;"))
			.split(",");
			int iw = Integer.parseInt(dimensions[0]);
			int ih = Integer.parseInt(dimensions[1]);
			int ow = Integer.parseInt(dimensions[2]);
			int oh = Integer.parseInt(dimensions[3]);
			int wOffset = ow-iw;
			int hOffset = oh-ih;
			sClient.getWebDriver().manage().window().setSize(new Dimension(width+wOffset,height+hOffset));
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
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	@Override
	public Action clone() {
		return new ResizeWindow(this);
	}
	

}
