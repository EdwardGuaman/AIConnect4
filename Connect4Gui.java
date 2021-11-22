import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 
 */

/**
 * @author EdwardGuaman
 *
 */
public class Connect4Gui {
	static boolean mefirst = false;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		final Hashtable<Character, Integer> letterToIndex = new Hashtable<Character, Integer>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put('A', 0);
				put('B', 1);
				put('C', 2);
				put('D', 3);
				put('E', 4);
				put('F', 5);
				put('G', 6);
				put('H', 7);
			}
		};
		final Hashtable<Integer, String> indexToLetter = new Hashtable<Integer, String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put(0, "A");
				put(1, "B");
				put(2, "C");
				put(3, "D");
				put(4, "E");
				put(5, "F");
				put(6, "G");
				put(7, "H");
			}
		};
		int[] fakeState = new int[64];
		int depth = 5;
		ArrayList<String> moveHistory = new ArrayList<>();
		AlphaBeta agent = new AlphaBeta();
		System.out.println("Please enter time limit(in seconds): ");
		String limit = sc.nextLine();
		agent.MAXTIME = Integer.parseInt(limit) * 1000;
		System.out.println();
		System.out.print("Who goes first, A for agent, O for opponent: ");
		String firstMove = sc.nextLine().toUpperCase();
		System.out.println();
		if (firstMove.equals("A")) {
			fakeState[28] = 1;
			moveHistory.add("D5");
			mefirst = true;
		}
		boolean win = false;
		while (!win) {
			print(fakeState, moveHistory);
			if (!moveHistory.isEmpty()) {
				System.out.print("Agent's move is: ");
				System.out.println(moveHistory.get(moveHistory.size() - 1));
			}
			System.out.print("Choose Opponent's next move: ");
			String currentMove = sc.nextLine().toUpperCase();
			System.out.println();
			moveHistory.add(currentMove.toUpperCase());
			char[] moveArray = currentMove.toCharArray();
			int rows = letterToIndex.get(moveArray[0]);
			int column = Character.getNumericValue(moveArray[1]);
			int realIndex = (rows) * 8 + (column - 1);
			fakeState[realIndex] = 2;
			int t = agent.first(fakeState, depth, -1000000000, 1000000000);
			fakeState[t] = 1;
			String ai = indexToLetter.get(t / 8);
			ai += (t % 8) + 1;
			moveHistory.add(ai);
		}
	}

	public static void print(int[] currentState, ArrayList<String> moveList) {
		if (mefirst) {
			System.out.println("  1 2 3 4 5 6 7 8   Agent vs. Opponent");
		}
		else {
			System.out.println("  1 2 3 4 5 6 7 8   Opponent vs. Agent");
		}
		String[] yAxis = { "A", "B", "C", "D", "E", "F", "G", "H" };
		Iterator<String> listIterator = moveList.iterator();
		int currentPosition = 0;
		for (int i = 0; i < yAxis.length; i++) {
			System.out.print(yAxis[i] + " ");
			for (int j = 0; j < 8; j++) {
				if (currentState[currentPosition] == 1) {
					System.out.print("X ");
					currentPosition++;
				} else if (currentState[currentPosition] == 2) {
					System.out.print("O ");
					currentPosition++;
				} else {
					System.out.print("- ");
					currentPosition++;
				}
			}
			System.out.print("\t");
			if (listIterator.hasNext()) {
				System.out.print((i + 1) + ". " + listIterator.next() + " ");
				if (listIterator.hasNext())
					System.out.print(listIterator.next());
			}
			System.out.println();
		}
		while (listIterator.hasNext()) {
			int moves = 9;
			System.out.print("\t\t\t");
			System.out.print((moves++) + ". " + listIterator.next() + " ");
			if (listIterator.hasNext())
				System.out.print(listIterator.next());
			System.out.println();
		}
		System.out.println("\n\n");
	}

}
