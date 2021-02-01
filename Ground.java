package flappybird;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;

import javax.imageio.ImageIO;
/**
 * Ground类 主要实现其后退以使小鸟前进
 * 用于MyPanel绘制需要的图片、坐标等成员变量应设为包内可见
 * @author 红豆冰大仙
 *
 */
public class Ground {
	BufferedImage image;//地板图片
	int x, y;//地板的left，top位置（此处用中心点无意义）
	int width, height;//图片长宽

	public Ground() throws Exception {//初始化有关参数
		image = ImageIO.read(new FileInputStream("image/ground.png"));
		width = image.getWidth();
		height = image.getHeight();
		x = 0;
		y = 500;
	}

	public void step() {//地板后退
		x--;
		if (x <= -111) {
			//后退到某一值时，重新初始化x，以实现下一轮后退
			x = 0;
		}
	}
}
