import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class AssoiationRule {
	// ������ �󵵼� -> �ŷڵ�
	// �ֵ��� �󵵼� -> ������
	final static int numItems=10000;
	static int topK,nBuckets;
	static double conf,supp;
	static HashMap<Pairs,Integer> hm=new HashMap<>();
	static int[] itemFreq=new int[numItems];
	static ArrayList<Map.Entry<Pairs,Integer>> al;
	
	public static void main(String[] args) throws FileNotFoundException{
		Scanner sc=new Scanner(System.in);
		System.out.println("�Է�(�����̸�, k, �ŷڵ�, ������): ");
		String fname=sc.next();
		topK=sc.nextInt();
		conf=sc.nextDouble(); // confidence : �ŷڵ�
		supp=sc.nextDouble(); // support : ������
		sc.close();
		
		int i,j,k;
		Pairs pa=new Pairs(-1,-1);
		Bucket bk=new Bucket();
		
		sc=new Scanner(new File(fname));
		nBuckets=sc.nextInt(); // ��ٱ����� ��
		for(i=0;i<nBuckets;i++) {
			bk.id=sc.nextInt();
			bk.nums=sc.nextInt();
			bk.items=new ArrayList<>(bk.nums);
			for(j=0;j<bk.nums;j++) {
				int item=sc.nextInt();
				bk.items.add(item);
				itemFreq[item]++; 
			}
			Collections.sort(bk.items);
			for(j=0;j<bk.nums-1;j++) {
				for(k=j+1;k<bk.nums;k++) {
					pa.small=bk.items.get(j); 
					pa.large=bk.items.get(k);
					if(hm.containsKey(pa))
						hm.put(pa, hm.get(pa)+1);
					else {
						Pairs new_pa=new Pairs(pa.small,pa.large);
						hm.put(new_pa, 1);
					}
				}
			}
			bk.items.clear();
		}
		al=new ArrayList<>(hm.entrySet());
		System.out.println("���� ��= "+al.size());
		Collections.sort(al,new ARComp());
		for(i=0;i<topK;i++) {
			System.out.println((i+1)+": "+al.get(i).getKey()+" "+al.get(i).getValue());
		}
		while(al.get(i-1).getValue().equals(al.get(i).getValue())) {
			System.out.println((i+1)+": "+al.get(i).getKey()+" "+al.get(i).getValue());
			i++; 
		}
		
		double limit=nBuckets*supp; 
		for(i=0;true;i++) {
			if(al.get(i).getValue()<limit)
				break; // �߰��ܰ迡�� �����ߴ�
			Pairs p=al.get(i).getKey();
			check_confidence(p.small,p.large,i); // x->y�� �ŷڵ� �˻�
			check_confidence(p.large,p.small,i); // y->x�� �ŷڵ� �˻�
		}
		if(sc!=null)
			sc.close();
	}
	
	static void check_confidence(int from,int to,int pid) {
		// ������� x->y�� �ŷڵ�=(x,y)�� �󵵼�/x�� �󵵼�
		// �Է� �ŷڵ����� ���� ������踦 ���
		double p_conf=al.get(pid).getValue()/(double)itemFreq[from];
		
		if(p_conf>=conf) {
			System.out.println(from+"->"+to);
			System.out.print(":������="+(al.get(pid).getValue()/(double)nBuckets));
			System.out.println(",�ŷڵ�="+p_conf);
		}
	}
	
	static class ARComp implements Comparator<Map.Entry<Pairs,Integer>>{

		@Override
		public int compare(Entry<Pairs, Integer> o1, Entry<Pairs, Integer> o2) {
			// -1:o1<o2, 0:o1==o2, 1:o1>o2 // ��������
			return o2.getValue().compareTo(o1.getValue()); // ��������
			// -1:o2<o1, 0:o2==o1, 1:o2>o1
		}
		
	}
}
