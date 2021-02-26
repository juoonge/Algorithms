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
	// 물건의 빈도수 -> 신뢰도
	// 쌍들의 빈도수 -> 지지도
	final static int numItems=10000;
	static int topK,nBuckets;
	static double conf,supp;
	static HashMap<Pairs,Integer> hm=new HashMap<>();
	static int[] itemFreq=new int[numItems];
	static ArrayList<Map.Entry<Pairs,Integer>> al;
	
	public static void main(String[] args) throws FileNotFoundException{
		Scanner sc=new Scanner(System.in);
		System.out.println("입력(파일이름, k, 신뢰도, 지지도): ");
		String fname=sc.next();
		topK=sc.nextInt();
		conf=sc.nextDouble(); // confidence : 신뢰도
		supp=sc.nextDouble(); // support : 지지도
		sc.close();
		
		int i,j,k;
		Pairs pa=new Pairs(-1,-1);
		Bucket bk=new Bucket();
		
		sc=new Scanner(new File(fname));
		nBuckets=sc.nextInt(); // 장바구니의 수
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
		System.out.println("쌍의 수= "+al.size());
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
				break; // 중간단계에서 실행중단
			Pairs p=al.get(i).getKey();
			check_confidence(p.small,p.large,i); // x->y의 신뢰도 검사
			check_confidence(p.large,p.small,i); // y->x의 신뢰도 검사
		}
		if(sc!=null)
			sc.close();
	}
	
	static void check_confidence(int from,int to,int pid) {
		// 상관관계 x->y의 신뢰도=(x,y)의 빈도수/x의 빈도수
		// 입력 신뢰도보다 높은 상관관계를 출력
		double p_conf=al.get(pid).getValue()/(double)itemFreq[from];
		
		if(p_conf>=conf) {
			System.out.println(from+"->"+to);
			System.out.print(":지지도="+(al.get(pid).getValue()/(double)nBuckets));
			System.out.println(",신뢰도="+p_conf);
		}
	}
	
	static class ARComp implements Comparator<Map.Entry<Pairs,Integer>>{

		@Override
		public int compare(Entry<Pairs, Integer> o1, Entry<Pairs, Integer> o2) {
			// -1:o1<o2, 0:o1==o2, 1:o1>o2 // 오름차순
			return o2.getValue().compareTo(o1.getValue()); // 내림차순
			// -1:o2<o1, 0:o2==o1, 1:o2>o1
		}
		
	}
}
