package assignment2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class nqueen {

	static int n;
	static int col[]; // 퀸의 위치 담는 변수  
	static int heuristic = 0; // 휴리스틱 점수  
	//static int cnt = 0;
	static boolean search; // 만족하는 결과를 찾았는지 참 거짓으로 판별 
	static int[] sol; // 결과를 찾았을 때 솔루션 저장 
	static String txt2 = ""; // 파일입출력을 위한 변수  

	public static boolean Attackable(int[] column) {
		// 퀸이 공격받는지 받지 않는지를 판별하는 함수로, nqueen을 만족하는지 검사할 때 사용
		// 만약 공격받는 위치라면 참, 아니라면 거짓 반환 
		column = new int[nqueen.n];
		for (int i = 0; i < nqueen.n; i++) {
			for (int j = i + 1; j < nqueen.n; j++) {
				if (i == j ) {
					//cnt++;
					return true;
				}
				if(column[i] == column[j]) {
					//cnt++;
					return true;
				}
				if(Math.abs(i - j) == Math.abs(column[i] - column[j])) {
					//cnt++;
					return true;
				}
			}
		}
		return false;
	}

	public static int getHeuristic(int[] col) {
		// heuristic의 점수를 계산해주는 함수 
		// Attackable과 비슷하게 구현
		// 공격받는 위치에 옮겨질 경우 점수를 올려줌 
		int heuristic = 0;
		for(int i = 0; i < nqueen.n; i++) {
			for(int j = i+1; j < nqueen.n; j++) {
				if(col[i] == col[j]) {
					heuristic++;
				}
				if(Math.abs(i - j) == Math.abs(col[i] - col[j])) {
					heuristic++;
				}
			}
		}
		return heuristic;
	}
	

	public static void chess() {
		// 랜덤하게 퀸을 위치시키는 초기 스테이트 함수 
		// Random 함수 사용 
		Random rd = new Random();
		for (int i = 0; i < nqueen.n; i++) {
			col[i] = rd.nextInt(nqueen.n);
		}
		/*if(Attackable(col)) {
			if(cnt > 1) {
				return;
			}
		}*/
	}

	public static void neighbor() {
		int colH;
		int bestH;
		int tmpH;
		int[] tmp = new int[nqueen.n];
		
		chess(); //col[]에 랜덤으로 퀸을 배치한 상태
		for(int i = 0; i < nqueen.n; i++) {
			//tmp를 사용하기위해 tmp에 하나씩 col의 퀸 정보를 복사 
			tmp[i] = col[i];
		}
		
		colH = getHeuristic(col);
		bestH = colH;
		tmpH = colH;
		
		// 이웃 설정은 현 상태에서 퀸을 한칸씩 밑으로 옮겨줌
		for (int i = 0; i < nqueen.n; i++) {
			for (int j = 0; j < nqueen.n; j++) {
				if(col[i] == j) // 원래 퀸이 놓여져있던 자리이면 휴리스틱 구할 필요 없으므로 continue 
					continue;
				// 퀸을 한 칸씩 옮겨보고 휴리스틱을 구한후 그 점수가 더 좋은 점수라면 변경해줌 
				col[i] = j;
				colH = getHeuristic(col);
				if (colH < bestH) {
					// 나보다 좋은 이웃이 있는 경우 
					// tmp가 best보다 작으면 best에 tmp를 넣고 col에 그 퀸 자리를 넣어줌 
					bestH = colH;
					tmp[i] = col[i];
				}
				else { // 좋은 점수가 아니라면 원래 자리로 되돌림 
					col[i] = tmp[i];
				}
			}
		}
		if(bestH == colH) {
			// 만약 휴리스틱의 가장 좋은값이 그대로 라면 local minimum에 빠진 상태이므로 다시 시작 
			chess();
			heuristic = getHeuristic(col);
			return;
		}
		else { // 그게 아니라 가장 좋은 점수가 저장이 되어있다면 휴리스틱에 저장 
			heuristic = bestH;
		}
	}

	public static void main(String[] args) throws IOException {
		int presentH;
		long before, after, runtime; // 실행시간 구하기 위한 변수 설정
		nqueen.sol = new int[n];
		
		nqueen.n = Integer.parseInt(args[0]); // 체스판 크기 입력받기 n*n
		
		before = System.currentTimeMillis();
		
		col = new int[n];
		chess();
		
		presentH = getHeuristic(col);
		
		while(presentH != 0) {
			neighbor(); // 현 상태에서 이웃을 구함 
			presentH = heuristic;
			if(presentH == 0) { 
				// 만약 neighbor에서 구해진 휴리스틱 점수가 0이어서 nqueen을 만족한다면 
				// 그 자리들을 출력하도록 한다 
				// 그게 아니라면 다시 반복문으로 들어와 restart 
				for (int i = 0; i < n; i++) {
					nqueen.txt2 = txt2 + col[i] + " ";
				}
				search = true; // 결과를 찾았으므로 서치를 참으로 바꿈 
				break;
			}	
		}
		
		// 파일 입출력 부분 
		after = System.currentTimeMillis();
		runtime = (after - before); // 시간차로 실행시간 저장  

		FileWriter fw = new FileWriter(args[1] + "/result" + args[0] + ".txt"); // 인자를 받고 경로에 저장하기 위함 

		String txt; // 텍스트에 나올 string
		

		if (nqueen.search == true) { //서치 결과로 n queen 문제 해결했을 시 실행 
			txt = ">Hill Climbing : \n";
			txt = txt + txt2;
			txt = txt + "\nTotal Elapsed Time : " + (double)runtime / 1000 + "\n\n";
		} else {
			txt = "\nNo solution\nTime : 0.0\n";
		}
		
		fw.write(txt);
		fw.close();
		
		
	}

}
