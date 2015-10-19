package game;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class Main {

	public static boolean inStartScreen;
	public final String rootDirectory = "D:\\Viktor\\Programming";
	public static Stage[] stages = new Stage[3];
	public static int currentStage = 0;
	public static int timeInStage = 0;
	public static ArrayList<BadGuy> badGuys = new ArrayList<BadGuy>();
	private static ArrayList<BadGuy> badGuysBuffer = new ArrayList<BadGuy>();
	public static Timer updateTimer = new Timer(40, new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			update(Gui.gui.getContentPane().getSize());
		}
		
	});
	
	public static Timer repaintTimer = new Timer(17, new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			Gui.gui.repaint();
		}
		
	});
	
	public static void main(String[] arg0){
		Gui.intializeGraphics();
		initializeStages();
		updateTimer.start();
		repaintTimer.start();
	}
	
	private static void initializeStages(){
		stages[0] = new Stage(new int[]{10});
		stages[1] = new Stage(new int[]{10, 100});
		stages[2] = new Stage(new int[]{10, 75, 200});
	}
	
	private static void update(Dimension contentSize){
		for (int i = 0; i < stages[currentStage].spawnTimes.length; i++) {
			if (stages[currentStage].spawnTimes[i] == timeInStage) {
				badGuysBuffer.add(new BadGuy());
			}
		}
		boolean[] isColumnOccupied = new boolean[4];
		float heightSizeOfABadGuy = contentSize.width / (float)16 / contentSize.height;
		for (int i = 0; i < badGuys.size(); i++) {
			BadGuy currentBadGuy = badGuys.get(i);
			currentBadGuy.y += 1/(float)512;
			if (currentBadGuy.y > 1 - heightSizeOfABadGuy) {
				System.out.println("Game over!!!!");
			}
			if (currentBadGuy.y < heightSizeOfABadGuy) {
				isColumnOccupied[currentBadGuy.x] = true;
			}
		}
		while(!isFull(isColumnOccupied) && !(badGuysBuffer.size() == 0)) {
			badGuysBuffer.get(0).x = takeFree(isColumnOccupied);
			badGuys.add(badGuysBuffer.get(0));
			badGuysBuffer.remove(0);
		}
		timeInStage++;
	}
	
	private static boolean isFull(boolean[] array) {
		for (int i = 0; i < array.length; i++) {
			if (!array[i]) {
				return false;
			}
		}
		return true;
	}
	
	private static int takeFree(boolean[] array) {
		int totalFree = 0;
		for (int i = 0; i < 4; i++) {
			if (!array[i]) {
				totalFree++;
			}
		}
		int[] freeIndexes = new int[totalFree];
		int nextIndex = 0;
		for (int i = 0; i < 4; i++) {
			if (!array[i]) {
				freeIndexes[nextIndex] = i;
				nextIndex++;
			}
		}
		int indexTaken = freeIndexes[(int) Math.floor(Math.random() * freeIndexes.length)];
		array[indexTaken] = true;
		return indexTaken;
	}
}
