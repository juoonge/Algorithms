import java.io.File;
import java.util.Scanner;

public class KMeansTest {
	public static void main(String[] args) throws Exception {
		Scanner sc=new Scanner(System.in);
		System.out.println("������ ���� �̸�? ");
		String fname=sc.nextLine();
		sc.close();
		
		// next() : ���๮�� �����ϰ� �Է¹���, ���� ��ū�� ���ڿ��� ����
		// nextLine():���๮�� �����ϴ� �� �����а�, ���๮�� ���� �������� ����
		
		sc=new Scanner(new File(fname));
		int N=sc.nextInt();
		int k=sc.nextInt();
		
		KMeans km=new KMeans(N,k);
		for(int i=0;i<N;i++)
			km.addElement(new Point(sc.nextDouble(),sc.nextDouble()));
		    
		km.initialize(); // �������� �ʱ� centroid�����, ù��° clustering ����
//		km.initializePlus(); // 
		System.out.println("�ʱ�ȭ �� Ŭ������ ����");
		System.out.println(km); // km.toString()
		
		for(int i=0;km.update()==true;i++) {
			// ���ο� centroid��� �� clustering, �� -> ������ false, �ٸ��� true
			System.out.println(i+"��° update �� Ŭ������ ����");
			System.out.println(km);
		}
		System.out.println("���� Ŭ������ ����");
		System.out.println(km);
		sc.close();
	}
}