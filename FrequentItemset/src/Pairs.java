public class Pairs {
	int small;
	int large;
	
	public Pairs(int a,int b) {
		if(a<=b) {
			small=a;
			large=b;
		}else {
			small=b;
			large=a;
		}
	}

	@Override
	public int hashCode() {
		return large*31+small;
	}

	@Override
	public boolean equals(Object o) {
		Pairs p=(Pairs)o;
		if(small==p.small&&large==p.large)
			return true;
		return false;
	}

	@Override
	public String toString() {
		return new String("["+small+", "+large+"]");
	}
}
