package flappybird;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
/**
 * 用于绘制游戏结束时显示的记分牌，显示当前成绩与最好成绩
 * @author 红豆冰大仙
 *
 */
public class ScoreCounterImg {
	private BufferedImage scoreImg; // 计分板图片

	public ScoreCounterImg() {//加载图片
		try {
			scoreImg = ImageIO.read(new FileInputStream("image/score.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void draw(Bird bird) {//使用Graphics2D绘制计分板上的分数
		Graphics2D graphics2d = scoreImg.createGraphics();
		// 绘制当前分数
		Font f = new Font(Font.MONOSPACED, Font.BOLD, 60);//设置字体、字号
		graphics2d.setFont(f);
		graphics2d.setColor(Color.white);//设置画笔颜色
		String str = Integer.toString(bird.scoreCounter.currentScore());//将当前分数转化为String
		graphics2d.drawString(str, 50, 108);//绘制分数，50 108分别为其left top坐标

		// 绘制最好成绩
		if (bird.scoreCounter.bestScore() > 0) {
			//当存在最好成绩时，绘制
			str = Integer.toString(bird.scoreCounter.bestScore());//将最好成绩转为String
			graphics2d.drawString(str, 174, 108);//绘制分数，174 108分别为其left top 坐标
		}

		graphics2d.dispose();//关闭graphic
		//将绘制完成的计分板储存为新文件
		try {
			File file = new File("image/scorebuf.png");
			ImageIO.write(scoreImg, "png", file);//将BufferredImage以png输出
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Draw score error!");
		}
	}
}
