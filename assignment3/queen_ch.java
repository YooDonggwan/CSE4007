package assignment3;

public class queen_ch {
	
	int n = 0; // 체스판 크기 n * n 
	int col[]; // 퀸의 위치 지정 
	int max_fitness = 0; // 최대 적합도 
	int[] check;  // 퀸이 어떤 퀸을 공격가능한 상태인지 체크하기 위한 변수 
	int fitness_value = 0; // 적합도  
	
	public queen_ch(int n){
		//생성자 
		this.n = n;
		this.col = new int[n];
		this.check = new int[n];
		this.max_fitness = n;
	}
	
	public int fitness() {
		
		fitness_value = max_fitness; // 초기 적합도를 최대로 놓고 공격가능한 경우 하나씩 빼줌 
		
		for(int i = 0; i < n; i++) {
			check[i] = 0; // 초기 체크상태 초기화 
		}
		
		int fitness_cnt = 0;
		// 적합도를 카운트해서 0보다 커질경우 다른 퀸을 공격할 수 있는 상태로 표시하기 위함 
		for(int i = 0; i < n; i++) {
			for(int j = i+1; j < n; j++) {
				if(col[i] == col[j]) 
					// 같은 열에 있을 경우 
					fitness_cnt++;
				else if(Math.abs(i - j) == Math.abs(col[i] - col[j]))
					// 같은 대각선 상에 있을 경우 
					fitness_cnt++;
			}
			if(fitness_cnt > 0) {
				// 다른 퀸을 공격할 수 있는 경우 그 퀸을 체크해줌 
				check[i]++;
			}
		}
		
		for(int i = 0; i < n; i++) {
			// 체크되는 퀸의 수만큼 최대 적합도에서 빼주므로 현재 적합도가 생성됨 
			fitness_value -= check[i];
		}	
		
		return fitness_value;
		
	}
	
}

