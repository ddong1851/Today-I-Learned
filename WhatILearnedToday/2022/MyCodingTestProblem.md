## 코딩테스트 문제

좌표 R, C에 거주하는 김 연구원은 차를 타고 출근하려고 한다. 인근 N명의 팀원들이 함께 차를 타고 가고 싶다고 한다. 

출근길이라 100미터 당 1분이 걸린다고 하고, 인접한 좌표 1칸이 100m 라고 할 때,

김 연구원은 최대 몇 명을 태워서, 최소 몇 시까지 출근할 수 있는지 구하시오(단, 무조건 지각인 경우 지각을 표출한다). 

### 조건
- 인접한 좌표 1칸 = 100m 
- 회사 위치: 0 <= X, Y <= 100
- 팀원의 수: 0 <= N <= 20
- 팀원의 위치: 0 <= P1, P2 <= 100
- 출발 시간: 00 <= HH <= 07, 00 <= MM <= 59
- 출근 시간: 08:00

#### 입력 예시

```
R C
X Y
N
P1 P2
...
HH MM
```

#### 출력 예시
```
M hh mm
```


```Java
package problemsolving;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class RoadToGIT {
	
	private static class Position {
		int X;
		int Y;
		int time;
		
		private Position(int X, int Y) {
			super();
			this.X = X;
			this.Y = Y;
			time = 0;
		}
		
		private Position(int X, int Y, int time) {
			super();
			this.X = X;
			this.Y = Y;
			this.time = time;
		}
	}
	
	private static int maxTime = convertToTime(8, 0);
	private static int minTime = Integer.MAX_VALUE;
	private static int maxTeamMates = 0;
	private static int teamMatesSize;
	private static int X, Y, R, C, N, HH, MM;

	public static void main(String[] args) throws Exception {
	
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		StringBuilder result = new StringBuilder();
		
		int TC = Integer.parseInt(st.nextToken());
		for(int tc = 1; tc <= TC; tc ++) {
			initValue();
			st = new StringTokenizer(br.readLine(), " ");
			R = Integer.parseInt(st.nextToken());
			C = Integer.parseInt(st.nextToken());
			ArrayList<Position> teamMates;
			
			st = new StringTokenizer(br.readLine(), " ");
			X = Integer.parseInt(st.nextToken());
			Y = Integer.parseInt(st.nextToken());
			
			st = new StringTokenizer(br.readLine(), " ");
			N = Integer.parseInt(st.nextToken());
			
			teamMates = new ArrayList<Position>();
			for (int i = 0; i < N; i ++) {
				st = new StringTokenizer(br.readLine(), " ");
				int P1 = Integer.parseInt(st.nextToken());
				int P2 = Integer.parseInt(st.nextToken());
				teamMates.add(new Position(P1, P2));
			}
			
			st = new StringTokenizer(br.readLine(), " ");
			HH = Integer.parseInt(st.nextToken());
			MM = Integer.parseInt(st.nextToken());
			int currentTime = convertToTime(HH, MM);
			
			String tcResult = "지각";
			if(timeToDestination(R, C, X, Y) <= maxTime - currentTime) {
				// 나 - 팀원 - 회사가 불가한 경우 제외
				int ableTime = maxTime - currentTime;
				for(int i = N - 1; i >= 0; i--) {
					Position teamMate = teamMates.get(i);
					int timeToTeamMate = timeToDestination(R, C, teamMate.X, teamMate.Y);
					int timeToDest = timeToDestination(teamMate.X, teamMate.Y, X, Y);
					if(timeToTeamMate + timeToDest > ableTime) {
						teamMates.remove(i);
					}
				}
				tcResult = solution(R, C, teamMates, currentTime);
			}
			result.append("#").append(tc).append(" ").append(tcResult).append("\n");
		}
		
		br.close();	
		System.out.println(result);
	}

	private static String solution(int r, int c, ArrayList<Position> teamMates, int currentTime) {		
		teamMatesSize = teamMates.size();
		dfs(r, c, teamMates, new boolean[teamMatesSize], currentTime, 0);
		return maxTeamMates + " " + convertTimeToHHMM(minTime);
	}

	private static void dfs(int r, int c, ArrayList<Position> teamMates, boolean[] isInCar, int currentTime, int picked) {
		if(currentTime > maxTime) return;
		
		int time = currentTime + timeToDestination(r, c, X, Y);
		if(time > maxTime) return;
		
		if(maxTeamMates < picked) {
			maxTeamMates = picked;
			minTime = time;
		} else if(maxTeamMates == picked && minTime > time) {
			minTime = time;
		}
		
		for(int i = 0; i < teamMatesSize; i ++) {
			if(!isInCar[i]) {
				isInCar[i] = true;
				Position teamMate = teamMates.get(i);
				dfs(teamMate.X, teamMate.Y, teamMates, isInCar, currentTime + timeToDestination(r, c, teamMate.X, teamMate.Y), picked+1);
				isInCar[i] = false;
			}
		}
	}

	private static void initValue() {
		minTime = Integer.MAX_VALUE;
		maxTeamMates = 0;
	}

	private static int convertToTime(int HH, int MM) {
		return HH*60 + MM;
	}
	
	private static int timeToDestination(int sR, int sC, int dR, int dC) {
		return Math.abs(sR - dR) + Math.abs(sC - dC);
	}
	
	private static String convertTimeToHHMM(int time) {
		String MM = time % 60 + "";
		String HH = time / 60 + "";
		if(HH.length() == 1) {
			HH = "0" + HH;
		}
		if(MM.length() == 1) {
			MM = "0" + MM;
		}
		return HH + MM;
	}

}
```

```Java
package problemsolving;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TestCaseGenerator {
	
	private static int R, C, X, Y, N, HH, MM;

	public static void main(String[] args) throws Exception {
		
		/*
		 * TC
		 * R C
		 * X Y
		 * N
		 * P1 P2
		 * ...
		 * HH MM
		 * ...repeat 
		 */
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int TC = Integer.parseInt(br.readLine());
		
		for(int i = 0; i < TC; i ++) {
			R = (int)Math.random()*100;
			C = (int)Math.random()*100;
			
			while(true) {
				// RC XY 가 일치하지 않는다면 탈출
				X = (int)Math.random()*100;
				Y = (int)Math.random()*100;
				if(R != X || C != Y) {
					break;
				}
			}
			
			N = (int)Math.random()*20;
			
		}
		
	}
	
	private static boolean hasSamePosition(ArrayList<int[]> positions, int pos) {
		int[] check = positions.get(pos);
		for(int i=0; i<positions.size(); i++) {
			if(i == pos) continue;
			int[] temp = positions.get(pos);
			if(check[0] == temp[0] && check[1] == temp[1]) {
				return true;
			}
		}
		return false;
	}

}

```
