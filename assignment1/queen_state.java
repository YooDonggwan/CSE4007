package nqueens;

public class queen_state {
	
	int[] col; //col[2] = 3 이면 (2,3)에 있다고 보자
	int num; // 행을 표
 
	queen_state(int n) { // 생성자 
		this.col = new int[n];
		this.num = 0;
	}
}
