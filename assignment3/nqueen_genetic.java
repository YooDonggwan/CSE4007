package assignment3;

import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
// 초기 염색체 생성 
// 초기 염색체 적합도 계산
// 현 염색체로부터 자손 생성 
// 생성 자손들 적합도 계산 v
// 종료조건 판별 
// 거짓? 다시 자손 생성 
// 참? 종료 

public class nqueen_genetic {
	
	static int n; // 체스판 크기 n * n 
	static int max_fitness = n; // 최대 피트니스는 n => 퀸이 공격가능한 상태인가 아닌가를 표시 
	static int population; // 세대별 자손 수   
	static boolean fit = false; //적합도 조건에 맞는지 표시 
	static boolean search; // 답을 찾았는지 표시 
	static int sol[]; 
	static String txt2 = "";
	
	
	public static queen_ch[] chromosome(int population) {
		// 초기 염색체들을 랜덤하게 생성하는 함수 
		// 그 수를 population으로 받아서 생성 
		Random rd = new Random();
		queen_ch[] chromo = new queen_ch[population];
		for(int i = 0; i < population; i++) {
			chromo[i] = new queen_ch(nqueen_genetic.n);
		}
		
		for(int i = 0; i < population; i++) {
			for(int j = 0; j < nqueen_genetic.n; j++) {
				// 랜덤하게 초기염색체를 생성하도록 함 
				chromo[i].col[j] = rd.nextInt(nqueen_genetic.n);
			}
		}
		
		return chromo;
	}
	
	public static queen_ch[] select(queen_ch[] parent) {
		// Roulette wheel selection
		// 내 fitness / 전체 fitness 확률로 선택 
		// 좋은게 나올 확률이 높을 뿐, 무조건 선택은 아님 
		Random rd = new Random();
		
		queen_ch[] new_parent = new queen_ch[2]; // 2개를 고를 것이므로 부모로 2개를 선택하도록 함 
		for(int i = 0; i < 2; i++) {
			new_parent[i] = new queen_ch(nqueen_genetic.n);
		}
		
		int sumFit = 0;
		for(int i = 0; i < parent.length; i++) {
			sumFit += parent[i].fitness(); // 모든 적합도 더해서 저장 
		}
		
		for(int i = 0; i < 2; i++) {
			int point = rd.nextInt(sumFit+1);
			int idx = 0;
			int lastidx = 0;
			int totalfit = 0;
			while(totalfit < point) { // 여기서 확률만큼의 선택권을 가지게 됨 
				totalfit += parent[idx].fitness();
				lastidx = idx;
				idx++;
			}
			new_parent[i] = parent[lastidx]; // 뽑힌 염색체를 저장 
			totalfit = 0;
			idx = 0;
			lastidx = 0;
			
			sumFit -= new_parent[i].fitness(); // 뽑힌 것의 적합도를 제외해줌 
		}
		
		
		return new_parent; // 2개의 염색체 부모로 반환 
	}
	
	public static queen_ch[] makeChild(int population, queen_ch[] parent) {
		// 자손 세대를 생성하는 함수 
		// 세대의 크기를 인자로 받아서 생성 
		Random rd = new Random();
		queen_ch[] new_parent = new queen_ch[2];
		for(int i = 0; i < 2; i++) {
			new_parent[i] = new queen_ch(nqueen_genetic.n);
		}
		new_parent = select(parent); // 2개의 parent를 골라줌  
		
		queen_ch[] child = new queen_ch[population];
		for(int i = 0; i < population; i++) {
			child[i] = new queen_ch(nqueen_genetic.n);
		}
		
		for(int i = 0 ; i < population; i++) {

			child[i] = cross_over(new_parent[0], new_parent[1]); // 부모의 염색체를 크로스오버해서 자식을 만들어줌 
			
			if(rd.nextInt(100) < 40) { // 40퍼센트 확률로 mutation 
				child[i] = mutation(child[i]);
			}
		}
		
		return child;
	}
	
	public static queen_ch cross_over(queen_ch chrom1, queen_ch chrom2) {
		// 부모의 염색체를 교차시켜서 자식을 만들기 위한 크로스오버 함수 
		// chrom1의 division 포인트까지 가져오고 거기부터 n까지 chrom2에서 가져옴
		queen_ch chrom = new queen_ch(nqueen_genetic.n);
		Random rd = new Random();
		int division_point = rd.nextInt(nqueen_genetic.n); // 랜덤으로 나누는 포인트 정해줌 
		
		for(int i = 0; i < division_point; i++) { // 처음부터 포인트까지 가져오고  
			chrom.col[i] = chrom1.col[i];
		}
		for(int i = division_point; i < n; i++) { // 포인트부터 마지막까지 가져옴 
			chrom.col[i] = chrom2.col[i];
		}
		
		return chrom; // 그렇게 생성된 자손 반환 
		
	}
	
	public static queen_ch mutation(queen_ch chrom) {
		// 돌연변이 생성함수
		Random rd = new Random();
		int i = rd.nextInt(nqueen_genetic.n); // 어느 위치의 퀸자리가 바뀔지 랜덤으로 결정 
		
		chrom.col[i] = rd.nextInt(nqueen_genetic.n); // 그 퀸이 어디로 갈지 랜덤으로 결정 
		
		return chrom; // 퀸의 자리가 바뀐 염색체를 반환 
	}
	
	
	public static void main(String args[]) throws IOException {
		
		long before, after, runtime; // 시간계산 위해 변수 선언 
		queen_ch[] new_parent = new queen_ch[2];
		
		nqueen_genetic.n = Integer.parseInt(args[0]);
		max_fitness = n;
		before = System.currentTimeMillis();
		
		queen_ch nqueen = new queen_ch(nqueen_genetic.n);

		population = n*n; // 세대별 인구수를 n*n으로 설정  
		queen_ch[] initial_chrom = new queen_ch[population];
		
		for(int i = 0; i < population; i++) {
			initial_chrom[i] = new queen_ch(nqueen_genetic.n);
		}
		
		queen_ch[] child = new queen_ch[population];
		for(int i = 0; i < population; i++) {
			child[i] = new queen_ch(nqueen_genetic.n);
		}
		
		initial_chrom = chromosome(population); // 초기 염색체들 생성 
		
		while(fit == false) { 
			// 종료조건은 퀸이 공격가능한게 아무도 없을 때 
			// 종료조건 만족시 반복문 빠져나감 
			new_parent = select(initial_chrom); // 부모 고르기 
			child = makeChild(population, new_parent); // 자손세대 생성 
			
			for(int i = 0; i < population; i++) {
				if(child[i].fitness() == max_fitness) {
					// 피트니스가 최대 피트니스와 같게 되면 종료 조건을 만족하는 것
					for(int j = 0; j < nqueen_genetic.n; j++) {
						nqueen.col[j] = child[i].col[j]; // 정답인 퀸의 위치를 nqueen에 저장 
					}
					fit = true; //적합도 만족으로 바꿈 
					search = true; // 답을 찾았으므로 true 
					break;
				}
			}
			
			if(fit == true) { 
				// 종료 만족시 반복문 빠져 나감 
				break;
			}
			
			//initial_chrom = child;
			
			// 종료조건 만족하지 못했을 때 
			for(int i = 0; i < population; i++) {
				for(int j = 0; j < nqueen_genetic.n; j++) {
					// 다시 intial_chrom에 현재 염색체들을 복사해서 위의 과정 반복 
					initial_chrom[i].col[j] = child[i].col[j];
				}
			}
		}
		for(int i = 0; i < nqueen_genetic.n; i++) {
			// 파일출력을 위해 txt2에 저장 
			nqueen_genetic.txt2 = txt2 + nqueen.col[i] + " ";
		}
		
		// 파일 입출력과 시간 계산 
		after = System.currentTimeMillis();
		runtime = (after - before); // 시간차로 실행시간 저장  
		
		FileWriter fw = new FileWriter(args[1] + "/result" + args[0] + ".txt"); // 인자를 받고 경로에 저장하기 위함 

		String txt; // 텍스트에 나올 string
		
		if (nqueen_genetic.search == true) { //서치 결과로 n queen 문제 해결했을 시 실행 
			txt = ">Genetic Algorithm : \n";
			txt = txt + txt2;
			txt = txt + "\nTotal Elapsed Time : " + (double)runtime / 1000 + "\n\n";
		} else {
			txt = "\nNo solution\nTime : 0.0\n";
		}

		fw.write(txt);
		fw.close();
		
	}
	
	
	

}
