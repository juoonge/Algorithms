import java.io.File;
import java.util.Scanner;

public class KMeansTest {
	public static void main(String[] args) throws Exception {
		Scanner sc=new Scanner(System.in);
		System.out.println("데이터 파일 이름? ");
		String fname=sc.nextLine();
		sc.close();
		
		// next() : 개행문자 무시하고 입력받음, 다음 토큰을 문자열로 리턴
		// nextLine():개행문자 포함하는 한 라인읽고, 개행문자 버린 나머지만 리턴
		
		sc=new Scanner(new File(fname));
		int N=sc.nextInt();
		int k=sc.nextInt();
		
		KMeans km=new KMeans(N,k);
		for(int i=0;i<N;i++)
			km.addElement(new Point(sc.nextDouble(),sc.nextDouble()));
		    
		km.initialize(); // 랜덤으로 초기 centroid만들고, 첫번째 clustering 수행
//		km.initializePlus(); // 
		System.out.println("초기화 후 클러스터 상태");
		System.out.println(km); // km.toString()
		
		for(int i=0;km.update()==true;i++) {
			// 새로운 centroid계산 후 clustering, 비교 -> 같으면 false, 다르면 true
			System.out.println(i+"번째 update 후 클러스터 상태");
			System.out.println(km);
		}
		System.out.println("최종 클러스터 상태");
		System.out.println(km);
		sc.close();
	}
}