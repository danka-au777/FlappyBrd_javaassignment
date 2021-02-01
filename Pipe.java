package flappybird;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.Random;

import javax.imageio.ImageIO;
/**
 * Pipe类，上下两根水管存在一张图中；
 * 实现两水管间隔等的参数的设置，并通过水管后退来实现（视觉上）小鸟前进
 * @author 红豆冰大仙
 *
 */

public class Pipe {
	BufferedImage image;//水管图片
	int x, y;// 中心点坐标
	int width, height;//图片长宽
	int gap;//上下两水管的间距（y方向）
	
	private int distance;//左右两列水管的间距（x方向）
	private Random random = new Random();//随机数

	public Pipe(int n) throws Exception {// 由水管参数（1或2）构造水管类，初始化有关参数
		image = ImageIO.read(new FileInputStream("image/column.png"));
		width = image.getWidth();
		height = image.getHeight();
		gap = 144;
		distance = 245;
		x = 300 + n * distance;//由参数决定当前位置，300决定游戏初始时不会马上出现水管
		y = random.nextInt(120) + 220;//通过随机数使水管高度变化，控制其在220-340内，使gap位置合理
	}

	public void step() {//水管后退（x左边改变）以实现小鸟前进，当退出1/2时改变x坐标重新出现水管
		x--;
		if (x <= -width / 2) {
			//当前水管退出自身宽度1/2
			x = distance * 2 - width / 2;//在当前水管间隔2倍处“产生新水管”
			y = random.nextInt(120) + 220;//随机改变y坐标
		}
	}
}
