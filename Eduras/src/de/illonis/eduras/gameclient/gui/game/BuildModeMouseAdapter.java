package de.illonis.eduras.gameclient.gui.game;

import java.awt.event.MouseEvent;

import de.illonis.eduras.gameclient.GuiInternalEventListener;

public class BuildModeMouseAdapter extends GuiMouseAdapter {

	public BuildModeMouseAdapter(GamePanelLogic logic,
			GuiInternalEventListener reactor) {
		super(logic, reactor);
	}

	private void buildModeClick(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			System.out.println("right click in build mode");
			// TODO: Send units
		} else if (e.getButton() == MouseEvent.BUTTON1) {
			System.out.println("left click");
		}
		// TODO: implement
	}

	@Override
	public void itemClicked(int slot) {
	}

	@Override
	public void mousePressed(MouseEvent e) {

		super.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		super.mouseReleased(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

}
