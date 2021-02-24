import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class KMeans {
	private int N,k;
	ArrayList<Point>alist;
	ArrayList<Point>centers;
	HashMap<Integer,HashSet<Point>> current_map;
	Random rand;
	
	public KMeans(int n,int k) {
		N=n;
		this.k=k;
		alist=new ArrayList<>(N);
		centers=new ArrayList<>();
		current_map=new HashMap<>();
		rand=new Random();
	}
	public void addElement(Point p) {
		alist.add(p);
	}
	public void initialize() {
		for(int i=0;i<k;i++) {
			int index=rand.nextInt(N);
			if(!centers.contains(alist.get(index))){
				centers.add(alist.get(index));
				i++;
			}
		}
		make_clusters(current_map);
	}
	public void initializePlus() {
		centers.add(alist.get(rand.nextInt(N)));
		for(int i=1;i<k;i++) {
			int index=longets(i);
			centers.add(alist.get(index));
		}
		make_clusters(current_map);
	}
	
	int longets(int cnum) {
		double max_d=0,dist;
		int max_i=-1,i,j;
		for(i=0;i<N;i++) {
			Point p=alist.get(i);
			dist=p.distance(centers.get(0));
			for(j=1;j<cnum;j++) {
				double temp=p.distance(centers.get(j));
				if(temp<dist)
					dist=temp;
			}
			if(dist>max_d) {
				max_d=dist;
				max_i=i;
			}
		}
		return max_i;
	}
	void make_clusters(HashMap<Integer,HashSet<Point>>hm) {
		int i,j;
		HashSet<Point> hs;
		
		for(i=0;i<k;i++)  // hm 초기화
			hm.put(i, new HashSet<Point>());
		
		for(i=0;i<N;i++) { // cluster에 추가
			Point p=alist.get(i);
			double min_d=p.distance(centers.get(0)); 
			int min_i=0;
			for(j=1;j<k;j++) {
				double temp=p.distance(centers.get(j));
				if(temp<min_d) {
					min_d=temp;
					min_i=j;
				}
			}
			hs=hm.get(min_i);
			hs.add(p); 
		}
	}
	public boolean update() {
		HashMap<Integer,HashSet<Point>> revised=new HashMap<>();
		revise_centers();
		make_clusters(revised);
		for(int i=0;i<k;i++) {
			if(!current_map.get(i).equals(revised.get(i))) {
				current_map=revised;
				return true;
			}
		}
		return false;
	}
	
	// current_map : 이전 cluster 정보
	// revised : 현재 cluster 정보  
	
	void revise_centers() {
		centers.clear();
		HashSet<Point> hs;
		for(int i=0;i<k;i++) {
			hs=current_map.get(i);
			double sumX=0,sumY=0;
			for(Point p:hs) {
				sumX+=p.getX();
				sumY+=p.getY();
			}
			centers.add(new Point(sumX/hs.size(),sumY/hs.size()));
		}
	}
	
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<k;i++) {
			//레퍼런스 변수를 출력하면, 자동으로 toString() 메소드가 호출됨.(자바의 암묵적 룰).
			sb.append("Cluster").append(i).append(": ").append(centers.get(i)).append(", size= ");
			sb.append(current_map.get(i).size()).append("\n");
		}
		return sb.toString();
	}
}