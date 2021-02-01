package flappybird;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import javax.imageio.ImageIO;
/**
 * Bird类，游戏主要逻辑实现都在于此，包括小鸟的飞行动态、自然下坠、是否撞击水管与地面等重要方法，都应设为public以便游戏调用；
 * 为方便MyPanel中绘图需要，应将当前图片image，图片坐标等参数为包内可见；其他参数为设置游戏初始条件用设为private
 * @author 红豆冰大仙
 *
 */
public class Bird {
	private double g;// 重力加速度gravity
	private double t;// 间隔时间
	private double s;// 竖直方向的位移
	private double v0;// 初始速度，向上
	private double speed;// 当前速度
	private int index;//图片参数，通过其改变实现小鸟扑翅膀动态
	private BufferedImage images[];//一组小鸟图片，翅膀动态不同
	
	ScoreCounter scoreCounter;//计分器
	int x, y;// 图片中心点位置,public绘制时需调用
	int width, height;//图片的宽度与高度，public绘制时需调用
	int size;// 碰撞矩形
	double alpha;// 用于在Graphics中rotate调整鸟头方向的角度，弧度制
	BufferedImage image;//当前小鸟的图片

	public Bird() throws Exception {//初始化各参数
		scoreCounter = new ScoreCounter();
		this.image = ImageIO.read(new FileInputStream("image/0.png"));
		this.images = new BufferedImage[8];
		x = 132;
		y = 275;
		size = 40;
		g = 5;
		t = 0.25;
		v0 = 15;
		s = 0;
		speed = v0;
		alpha = 0;
		width = image.getWidth();
		height = image.getHeight();
		for (int i = 0; i < 8; i++) {
			images[i] = ImageIO.read(new FileInputStream("image/" + i + ".png"));
		}
		index = 0;
	}

	public void flappy() {//使当前速度为初始速度，从而向上飞
		speed = v0;
	}

	public void fly() {// 改变图片参数，实现鸟飞行时翅膀变化
		index++;
		image = images[(index/12) % 8];//index/12调整帧率
	}

	public void step() {//竖直方向自然下坠
		double vi = speed;
		s = vi * t - g * t * t / 2;// 通过物理公式得到竖直方向的位移
		y = y - (int) s;//改变y使小鸟向下
		double vc = vi - g * t;// 当前速度
		speed = vc;
		alpha = -Math.atan(s / 8);//通过反正切得到倾角
	}

	public boolean hit(Ground grd) {//检查鸟是否撞击地板
		boolean hit = y + size / 2 > grd.y;// 碰撞矩形的bottom在地面top之上，hit为假
		if (hit) {
			// 碰撞矩形接触地面
			this.y = grd.y - this.size / 2;// 将鸟放在地面上
			this.alpha = -Math.PI / 2; // 改变倾角，实现一头栽在地上的效果
		}
		return hit;
	}
	public boolean hit(Pipe pipe) {// 检查鸟是否撞击水管
		if (x > pipe.x - pipe.width / 2 - size / 2 && x < pipe.x + pipe.width / 2 + size / 2) {
			// 检查鸟是否在水管宽度范围内（通过x）
			if (y > pipe.y - pipe.gap / 2 + size / 2 && y < pipe.y + pipe.gap / 2 - size / 2) {
				//检查鸟是否碰撞水管边缘（通过y）
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean isPass(Pipe pipe1, Pipe pipe2) {//检查鸟是否通过水管，由于panel内一次会出现两根水管，传入两个Pipe
		if (this.x == pipe1.x || this.x == pipe2.x) {
			//中心对齐，视为通过一次
			scoreCounter.count();//调用积分器的count方法，加一分
			return true;
		} else {
			return false;
		}
	}
}
