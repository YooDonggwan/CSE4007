from z3 import *
import time

#Number of queens
print("N: ")
N = int(input())

start = time.time()

#Var
# 1차원 배열로 퀸의 위치를 표시할 수 있는 변수 선언 
col = [Int("x_%s" % (row)) for row in range(N)]

#Const
# j는 열의 위치이고, i는 행의 위치이며 값 설정을 먼저 하기위해 j먼저 넣어줌 
domain = [Or({col[i] == j for j in range(N)}) for i in range(N)] 

# 행과 열에 제약조건을 동시에 줌
# 두 개의 행이 같지 않으면 두개의 열도 같지 않아야 함
rowcolConst = [Implies(i != j, col[i] != col[j]) for i in range(N) for j in range(N)]

# 대각선 제약조건
# 대각선 제약조건 1은 ( \ ) 이 방향의 조건
diagConst_1 = [Implies(i != j, col[j] - col[i] != j - i) for i in range(N) for j in range(N)]

# 대각선 제약조건 2는 ( / ) 이 방향의 조건
diagConst_2 = [Implies(i != j, col[j] - col[i] != i - j) for i in range(N) for j in range(N)]

# 모든 제약조건들을 합침 
nqueen_const = domain + rowcolConst + diagConst_1 + diagConst_2

s = Solver()
s.add(nqueen_const)

if s.check() == sat:
	m = s.model()
	#r = [m.evaluate(col[i] + 1) for i in range(N)] # 결과에 0을 없애고 1부터 시작하는 결과로 나타내기 위해 1을 더해
	#print_matrix(r)
	print([m.evaluate(col[i] +1 ) for i in range(N)]) # 출력을 가로로 하기 위해 그냥 print 사용함

print("elapsed time: ", time.time() - start, " sec")