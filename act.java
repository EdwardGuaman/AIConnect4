import java.util.Arrays;

/**
 * 
 */

/**
 * @author EdwardGuaman
 *
 */
public class act {
	private int[] state;
	public int move;
	
	public act(int[] s, int m) {
		state = Arrays.copyOf(s, s.length);
		move = m;
	}
	
	
	public int[] getState() {
		return state;
	}
	public void setState(int[] state) {
		this.state = state;
	}
	public int getMove() {
		return move;
	}
	public void setMove(int move) {
		this.move = move;
	}

}
