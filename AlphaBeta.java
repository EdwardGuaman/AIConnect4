import java.util.Arrays;
import java.util.LinkedList;

/**
 * 
 */

/**
 * @author EdwardGuaman
 *
 */
public class AlphaBeta {
	double startTime = 0;
	int realindex = -1;
	int[] maxstate = null;
	int MAXTIME = 0;

	LinkedList<act> listOfFirst = new LinkedList<>();

	public int first(int[] state, int depth, int alpha, int beta) {
		int max = -100000000;
		startTime = System.currentTimeMillis();
		for (int i = 0; i < state.length; i++) {
			if (state[i] == 0) {
				int[] temp = Arrays.copyOf(state, state.length);
				temp[i] = 1;
				listOfFirst.add(new act(temp, i));
			}
		}
		while (!listOfFirst.isEmpty()) {
			act temp = listOfFirst.pop();
			int val = ab(temp.getState(), depth, alpha, beta, false);
			if (val > max) {
				max = val;
				realindex = temp.getMove();
			}
		}
		return realindex;
	}

	public int ab(int[] state, int depth, int alpha, int beta, boolean maxP) {
		if (depth == 0 || TerminalTest(state) == true || (System.currentTimeMillis() - startTime >= MAXTIME)) {
			return Utility(state, maxP);
		}
		if (maxP) {
			int v = -32000;
			LinkedList<int[]> actions = Actions(state, maxP);
			while (!actions.isEmpty()) {
				v = Max(v, ab(actions.pop(), depth - 1, alpha, beta, false));
				alpha = Max(alpha, v);
				if (alpha >= beta)
					break;
			}
			return v;
		} else {
			int v = 32000;
			LinkedList<int[]> actions = Actions(state, maxP);
			while (!actions.isEmpty()) {
				v = Min(v, ab(actions.pop(), depth - 1, alpha, beta, true));
				beta = Min(beta, v);
				if (alpha >= beta)
					break;
			}
			return v;
		}
	}

	private LinkedList<int[]> Actions(int[] state, boolean maxP) {
		LinkedList<int[]> re = new LinkedList<>();
		for (int i = 0; i < state.length; i++) {
			if (state[i] == 0) {
				int[] temp = Arrays.copyOf(state, state.length);
				if (maxP) {
					temp[i] = 1;
				} else {
					temp[i] = 2;
				}
				re.add(temp);
			}
		}
		return re;
	}

	private int Min(int v, int ab) {
		return (v < ab) ? v : ab;
	}

	private int Max(int v, int ab) {
		return (v > ab) ? v : ab;
	}

	private int Utility(int[] state, boolean turn) {
		int[] board = new int[64];
		for (int i = 0; i < state.length; i++) {
			int current = state[i];
			if (current == 0)
				continue;
			int x = i;
			int count = 1;
			int topcount = 0;
			int bottomcount = 0;
			int rightcount = 0;
			int leftcount = 0;
//			go up so long as it is a valid position and the value of the position is the same as the starting position's value
			while (vert(x - 8) && state[x - 8] == current) {
				count++;
				x -= 8;
				if (count == 2) {
//					two connected, with two open,	
					if ((vert(x - 8) && vert(i + 8)) && (state[x - 8] == 0 && state[i + 8] == 0)) {
//						max turn with two 
						if (current == 1) {
							board[x - 8] += 1000;
							board[i + 8] += 1000;
						}
//						min turn with two 
						else if (current == 2) {
							board[x - 8] += -1000;
							board[i + 8] += -1000;
						}
					}
				}
				if (count == 3) {
//					bottom is blocked by wall or piece
					if (!vert(i + 8) || (vert(i + 8) && state[i + 8] != 0)) {
						// 3 connected top open
						if (vert(x - 8) && state[x - 8] == 0) {
							if (current == 1) {
								board[x - 8] += 9000;
							} else if (current == 2) {
								board[x - 8] += -9000;
							}
						}
//					top is blocked by wall or piece
						else if (!vert(x - 8) || (vert(x - 8) && state[x - 8] != 0)) {
//						3 connected bottom open
							if (vert(i + 8) && state[i + 8] == 0) {
//							my turn
								if (current == 1) {
									board[i + 8] += 9000;
								} else if (current == 2) {
									board[i + 8] += -9000;
								}
							}
						}
					}
				}
				if (count == 4) {
					if (current == 1) {
						return 100000;
					} else if (current == 2) {
						return -100000;
					}
				}
			}
//			resetting----------------------------------------------------------------------------------------------------
			topcount = count;
			count = 1;
			x = i;
//			go down so long as it is a valid position and the value of the position is the same as the starting position's value
			while (vert(x + 8) && state[x + 8] == current) {
				count++;
				x += 8;
				if (count == 2) {
//					two connected, with two open,	may later need to add case for two connected one open
					if ((vert(x + 8) && vert(i - 8)) && (state[x + 8] == 0 && state[i - 8] == 0)) {
//						max turn with two op
						if (current == 1) {
							board[x + 8] += 1000;
							board[i - 8] += 1000;
						}
//						min turn with two me
						else if (current == 2) {
							board[x + 8] += -1000;
							board[i - 8] += -1000;
						}
					}
				}
				if (count == 3) {
//					top is blocked by wall or piece
					if (!vert(i - 8) || (vert(i - 8) && state[i - 8] != 0)) {
						// 3 connected top open
						if (vert(x + 8) && state[x + 8] == 0) {
							// max turn with 3 enemy
							if (current == 1) {
								board[x + 8] += 9000;
							} else if (current == 2) {
								board[x + 8] += -9000;
							}
						}
					}
//					bottom is blocked by wall or piece
					else if (!vert(x + 8) || (vert(x + 8) && state[x + 8] != 0)) {
//						3 connected bottom open
						if (vert(i - 8) && state[i - 8] == 0) {
//							my turn
							if (current == 1) {
								board[i - 8] += 9000;
							}
//							enemy turn 3 me
							else if (current == 2) {
								board[i - 8] += -9000;
							}
						}
					}
//					3 connected 2 open = win
					else if (vert(x + 8) && vert(i - 8) && state[x + 8] == 0 && state[i - 8] == 0) {
						if (current == 1) {
							return 100000;
						} else {
							return -100000;
						}
					}
				}
				if (count == 4) {
					if (current == 1) {
						return 100000;
					} else if (current == 2) {
						return -100000;
					}
				}
			}
//			resetting--------------------------------------------------------------------------------------------------------------
			bottomcount = count;
			count = 1;
			x = i;
//			go right so long as it is a valid position and the value of the position is the same as the starting position's value
			while (horizon(i, x + 1) && state[x + 1] == current) {
				count++;
				x++;
				if (count == 2) {
					if (horizon(i, x + 1) && horizon(i, i - 1) && state[x + 1] == 0 && state[i - 1] == 0) {
						if (current == 1) {
							board[x + 1] += 1000;
							board[i - 1] += 1000;
						} else if (current == 2) {
							board[x + 1] += -1000;
							board[i - 1] += -1000;
						}
					}
				}
				if (count == 3) {
					if (!horizon(i, x + 1) || ((horizon(i, x + 1)) && state[x + 1] != 0)) {
//						three connected right blocked by wall or piece
						if (horizon(i, i - 1) && state[i - 1] == 0) {
							if (current == 1) {
								board[i - 1] += 9000;
							} else if (current == 2) {
								board[i - 1] += -9000;
							}
						}
					} else if (!horizon(i, i - 1) || ((horizon(i, i - 1)) && state[i - 1] != 0)) {
//						three connected left blocked by wall or piece
						if (horizon(i, x + 1) && state[x + 1] == 0) {
							if (current == 1) {
								board[x + 1] += 9000;
							} else if (current == 2) {
								board[x + 1] += -9000;
							}
						}
					} else if (horizon(i, x + 1) && horizon(i, i - 1) && state[x + 1] == 0 && state[i - 1] == 0) {
						if (current == 1) {
							return 100000;
						} else {
							return -100000;
						}
					}

				}
				if (count == 4) {
					if (current == 1) {
						return 100000;
					} else if (current == 2) {
						return -100000;
					}
				}
			}
//			resetting--------------------------------------------------------------------------------------------------------------
			rightcount = count;
			count = 1;
			x = i;
//			go left so long as it is a valid position and the value of the position is the same as the starting position's value
			while (horizon(i, x - 1) && state[x - 1] == current) {
				count++;
				x--;
				if (count == 2) {
					if ((horizon(i, x - 1) && horizon(i, i + 1)) && state[i + 1] == 0 && state[x - 1] == 0) {
						if (current == 1) {
							board[i + 1] += 1000;
							board[x - 1] += 1000;
						} else if (current == 2) {
							board[i + 1] += -1000;
							board[x - 1] += -1000;
						}
					}

				}
				if (count == 3) {
					if (!horizon(i, x - 1) || ((horizon(i, x - 1) && state[x - 1] != 0))) {
//						three connected left blocked by wall or piece
						if (horizon(i, i + 1) && state[i + 1] == 0) {
							if (current == 1) {
								board[i + 1] += 9000;
							} else if (current == 2) {
								board[i + 1] += -9000;
							}
						}
					} else if (!horizon(i, i + 1) || ((horizon(i, i + 1) && state[i + 1] != 0))) {
						if (horizon(i, x - 1) && state[x - 1] == 0) {
							if (current == 1) {
								board[x - 1] += 9000;
							} else if (current == 1) {
								board[x - 1] += -9000;
							}
						}
					}
				}
				if (count == 4) {
					if (current == 1) {
						return 100000;
					} else if (current == 2) {
						return -100000;
					}
				}
			}
//			post check check
			leftcount = count;
//			convert counts to index and check if empty or blocked
//			may need to update weights for when two pieces are seperated by a blank
			int topcounti = i - (8 * topcount);
			int bottomcounti = i + (8 * bottomcount);
			int rightcounti = i + rightcount;
			int leftcounti = i - leftcount;

			if (vert(topcounti) && state[topcounti] == 0) {
				if (topcount == 2) {
					if (vert(topcounti + 8)) {
						if (state[topcounti + 8] == current) {
							if (current == 1) {
								if (turn) {
									return 100000;
								}
								board[topcounti] += 900;
							} else {
								if (!turn) {
									return -100000;
								}
								board[topcounti] += -900;
							}
						}
					}
				}
//				
				if (current == 1) {
					board[topcounti] += 100;
				} else {
					board[topcounti] += -100;
				}
			}
			if (vert(bottomcounti) && state[bottomcounti] == 0) {
				if (bottomcount == 2) {
					if (vert(bottomcounti - 8)) {
						if (state[bottomcounti - 8] == current) {
							if (current == 1) {
								if (turn) {
									return 100000;
								}
								board[bottomcounti] += 900;
							} else {
								if (!turn) {
									return -100000;
								}
								board[bottomcounti] += -900;
							}
						}
					}
				}

				if (current == 1) {
					board[bottomcounti] += 100;
				} else {
					board[bottomcounti] += -100;
				}
			}
			if (horizon(i, rightcounti) && state[rightcounti] == 0) {
				if (rightcount == 2) {
					if (horizon(i, rightcounti + 1)) {
						if (state[rightcounti + 1] == current) {
							if (current == 1) {
								if (turn) {
									return 100000;
								}
								board[rightcounti] += 900;
							} else {
								if (!turn) {
									return -100000;
								}
								board[rightcounti] += -900;
							}
						}
					}
				}

				if (current == 1) {
					board[rightcounti] += 100;
				} else {
					board[rightcounti] += -100;
				}
			}
			if (horizon(i, leftcounti) && state[leftcounti] == 0) {
				if (leftcount == 2) {
					if (horizon(i, leftcounti - 1)) {
						if (state[leftcounti - 1] == current) {
							if (current == 1) {
								if (turn) {
									return 100000;
								}
								board[leftcounti] += 900;
							} else {
								if (!turn) {
									return -100000;
								}
								board[leftcounti] += -900;
							}
						}
					}
				}

				if (current == 1) {
					board[leftcounti] += 100;
				} else {
					board[leftcounti] += -100;
				}
			}
		}
//		TODO change max and min to sum and remember to return a move/index
		return sum(board);
	}

	private int sum(int[] board) {
		int sum = 0;
		for (int i = 0; i < board.length; i++) {
			sum += board[i];
		}
		return sum;
	}

	private boolean TerminalTest(int[] state) {
		for (int i = 0; i < state.length; i++) {
			int current = state[i];
			if (current == 0)
				continue;
			int x = i;
			int count = 1;
//			go up so long as it is a valid position and the value of the position is the same as the starting position's value
			while (vert(x - 8) && state[x - 8] == current) {
				count++;
				x -= 8;
				if (count == 3) {
					return true;
				}
			}
			count = 1;
			x = i;
//			go down so long as it is a valid position and the value of the position is the same as the starting position's value
			while (vert(x + 8) && state[x + 8] == current) {
				count++;
				x += 8;
				if (count == 3) {
					return true;
				}
			}
			count = 1;
			x = i;
//			go right so long as it is a valid position and the value of the position is the same as the starting position's value
			while (horizon(i, x + 1) && state[x + 1] == current) {
				count++;
				x++;
				if (count == 3) {
					return true;
				}
			}
			count = 1;
			x = i;
//			go right so long as it is a valid position and the value of the position is the same as the starting position's value
			while (horizon(i, x - 1) && state[x - 1] == current) {
				count++;
				x--;
				if (count == 3) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean horizon(int i, int j) {
		if (!vert(j)) {
			return false;
		}
		if (i > 55) {
			return (j > 55);
		} else if (i < 56 && i > 47) {
			return (j > 47 && j < 56);
		} else if (i < 48 && i > 39) {
			return (j > 39 && j < 48);
		} else if (i < 40 && i > 31) {
			return (j > 32 && j < 40);
		} else if (i < 32 && i > 15) {
			return (j > 15 && j < 32);
		} else if (i < 16 && i > 7) {
			return (j > 7 && j < 16);
		} else {
			return (j < 8);
		}
	}

	private boolean vert(int i) {
		return (i >= 0) && (i < 64);
	}
}
