package nqueens;

import java.util.Scanner;
import java.util.Stack; // 자바 스택 라이브러리 사용 
import java.io.FileWriter;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Queue; // 자바 큐, 링크드리스트 라이브러리 사용 
import java.util.LinkedList;

public class main {
	
	static String txt2="";
	static int n; // 체스판 크기
	static boolean search; // 서치를 완료했는지 저장 
	static int[] sol; // 서치 결과 저장
	
	public static void main(String[] args) throws IOException {
		long before, after, runtime; // 실행시간 구하기 위한 변수 설정
		main.sol = new int[n];
		
		main.n = Integer.parseInt(args[0]); // 체스판 크기 입력받기 n*n
		
		before = System.currentTimeMillis();
		DFS_search(n); // dfs
		
		after = System.currentTimeMillis();
		runtime = (after - before); // 시간차로 실행시간 저장  

		FileWriter fw = new FileWriter(args[1] + "/result" + args[0] + ".txt"); // 인자를 받고 경로에 저장하기 위함 

		String txt; // 텍스트에 나올 string

		if (main.search == true) { //서치 결과로 n queen 문제 해결했을 시 실행 
			txt = ">DFS\nLocation : ";
			txt = txt + txt2;
			
			txt = txt + "\nTime : " + (double)runtime / 1000 + "\n\n";
		} else {
			txt = ">DFS\nNo solution\nTime : 0.0\n";
		}
		
		fw.write(txt);
		

		
		// 이전 출력과 같음 
		txt2 = "";
		before = System.currentTimeMillis();
		BFS_search(n); // bfs
		after = System.currentTimeMillis();
		runtime = (after - before); // 위와 같
		if (main.search == true) {
			txt = ">BFS\nLocation : ";
			txt = txt + txt2;
			
			txt = txt + "\nTime : " + (double)runtime / 1000 + "\n\n";
		} else {
			txt = ">BFS\nNo solution\nTime : 0.0\n";
		}
		fw.write(txt);

		// 종류만 다르고 이전 출력과 같음 
		txt2 = "";
		before = System.currentTimeMillis();
		DFID_search(n); // dfid 
		after = System.currentTimeMillis();
		runtime = (after - before);
		if (main.search == true) {
			txt = ">DFID\nLocation : ";
			txt = txt + txt2;
			
			txt = txt + "\nTime : " + (double)runtime / 1000 + "\n\n";
		} else {
			txt = ">DFID\nNo solution\nTime : 0.0\n";
		}
		fw.write(txt);
		fw.close();

	}
	
	

	public static void DFS_search(int n) { // dfs는 스택으로

		Stack<queen_state> stack = new Stack<>(); // 스택에 클래스를 넣기위해 클래스형 선언
		queen_state queens = new queen_state(n); // 초기 상태 객체 생성 
		queen_state tmp = new queen_state(n); // 퀸을 놓을 때마다 덮어 저장할 객체 생성 

		stack.push(queens); // 처음 빈 체스판 푸시 
		// System.out.println(stack.size());
		
		
		while (!stack.isEmpty()) {
			tmp = stack.pop(); // 스택에서 하나를 꺼내 tmp에 저장 

			if (tmp.num < n) { // 퀸을 다 안 놓은 경우
				for (int i = 0; i < n; i++) {
					// 새로운 객체를 계속 생성하여 이전 결과를 이어 받고 새로 퀸을 추가함 
					queen_state state = new queen_state(n);
					for (int j = 0; j < tmp.num; j++) { // num 변수로 몇 행부터 퀸을 다시 놓아야 하는지 정해짐 
						state.col[j] = tmp.col[j];
					}
					// 퀸이 들어갈 자리 저
					state.num = tmp.num; 
					state.col[state.num] = i;
					state.num++;
					stack.push(state);
				}
			} // 퀸 다 놓은 경우
			else if (Possible(tmp.col)) { // 퀸이 모두 놓아졌을 때 n queen을 만족하는지 판
				for (int k = 0; k < n; k++) {
					main.txt2 = txt2 + tmp.col[k]+" "; // txt에 출력하기 위해 저장   
				}
				search = true; // 완료시 search를 true로 변경  
				break;
			}

		}
	}

	public static void BFS_search(int n) { 
		// bfs는 큐로
		// dfs 와 차이는 스택 큐밖에 없으므로 스택과 큐만 변경하면 됨 
		Queue<queen_state> queue = new LinkedList<>();

		queen_state queens = new queen_state(n);
		queen_state tmp = new queen_state(n);

		queue.add(queens);
		

		while (!queue.isEmpty()) {
			tmp = queue.remove();

			if (tmp.num < n) { // 퀸을 다 안 놓은 경우
				for (int i = 0; i < n; i++) {
					queen_state state = new queen_state(n);
					for (int j = 0; j < tmp.num; j++) {
						state.col[j] = tmp.col[j];
					}
					state.num = tmp.num;
					state.col[state.num] = i;
					state.num++;
					queue.add(state);
				}
			} // 퀸 다 놓은 경우
			else if (Possible(tmp.col)) {
				for (int k = 0; k < n; k++) {
					main.txt2 = txt2 + tmp.col[k]+" ";
				}
				search = true;
				break;
			}

		}
	}

	public static void DFID_search(int n) { // depth기반 dfs

		Stack<queen_state> stack = new Stack<>(); // 스택에 클래스를 넣기위해 클래스형 선언
		queen_state queens = new queen_state(n);
		queen_state tmp = new queen_state(n);

		for (int depth = 0; depth <= n; depth++) { 
			// dfid는 깊이별로 dfs를 진행하므로 깊이를 나누어 dfs를 실행 
			stack.push(queens);

			while (!stack.isEmpty()) {
				tmp = stack.pop();

				if (tmp.num < depth) { 
					// 퀸을 다 안 놓은 경우
					for (int j = 0; j < depth; j++) {
						queen_state state = new queen_state(n);
						for (int h = 0; h < tmp.num; h++) {
							state.col[h] = tmp.col[h];
						}
						state.num = tmp.num;
						state.col[state.num] = j;
						state.num++;
						stack.push(state);
					}
				} 
				// 퀸 다 놓은 경우
				else if (Possible(tmp.col)) {
					for (int k = 0; k < depth; k++) {
						main.txt2 = txt2 + tmp.col[k]+" ";
					}
					search = true;
					break;
				}
			}
			stack.clear(); // depth 하나를 갈 때 마다 다시 해야하므로 스택을 클리어 시킴  

		}

	}

	public static boolean Possible(int[] col) { // 퀸이 n queen을 만족하는지 판별 
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				// 같은 열에 있을 때 / 같은 대각선 상에 있을 때로 판별  
				if (col[i] == col[j] || Math.abs(j - i) == Math.abs(col[j] - col[i])) {
					return false;
				}
			}
		}
		return true;
	}

}
