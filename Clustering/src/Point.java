public class Point{
	private double x,y;
	
	public Point(double x,double y) {
		this.x=x;
		this.y=y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double distance(Point p) {
		return Math.sqrt((p.x-x)*(p.x-x)+(p.y-y)*(p.y-y));
	}

	@Override
	public int hashCode() {
		final int prime=31;
		int result=1;
		long temp;
		// 64비트 double 데이터를 64비트 long 데이터로 변환하여 비트시프트 및 XOR같은 작업이 작동하도록 한다.
		temp=Double.doubleToLongBits(x);
		// long 상위 32비트와 하위 32비트를 XOR
		result=prime*result+(int)(temp^(temp>>>32));
		temp=Double.doubleToLongBits(y);
		result=prime*result+(int)(temp^(temp>>>32));
		return result;
	}

	@Override
	public String toString() {
		return "[x= "+x+", y= "+y+"]";
	}
}