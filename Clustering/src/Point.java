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
		// 64��Ʈ double �����͸� 64��Ʈ long �����ͷ� ��ȯ�Ͽ� ��Ʈ����Ʈ �� XOR���� �۾��� �۵��ϵ��� �Ѵ�.
		temp=Double.doubleToLongBits(x);
		// long ���� 32��Ʈ�� ���� 32��Ʈ�� XOR
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