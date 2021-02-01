package flappybird;
/**
 * code by UTF-8
 */

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import javax.swing.*;
import javax.imageio.ImageIO;

/**
 * 主类，实现窗体创建、游戏逻辑连接等
 * @author 红豆冰大仙
 *
 */

class MyPanel extends JPanel {
	BufferedImage background;//背景图片
	BufferedImage gameStartImg;//启动图片
	BufferedImage gameOverImg;//结束图片
	BufferedImage scoreCurImg;//计分牌
	Bird bird;//鸟对象
	Ground ground;//地板
	Pipe pipe1, pipe2;//两根水管
	int score = 0;
	int speed;//控制休眠时间，以改变游戏难度
	boolean gameOver, start;//游戏状态

	public MyPanel() throws Exception {//初始化有关参数
		speed = 60;
		gameStartImg = ImageIO.read(new FileInputStream("image/start.png"));
		background = ImageIO.read(new FileInputStream("image/bg.png"));
		gameOverImg = ImageIO.read(new FileInputStream("image/gameover.png"));
		ground = new Ground();
		pipe2 = new Pipe(1);
		pipe1 = new Pipe(2);
		bird = new Bird();
		gameOver = false;
	}

	public void paint(Graphics g) {//绘制游戏全界面
		g.drawImage(background, 0, 0, null);//绘制背景
		
		//绘制两水管，由中心点坐标计算得left top坐标
		g.drawImage(pipe1.image, pipe1.x - pipe1.width / 2, pipe1.y - pipe1.height / 2, null);
		g.drawImage(pipe2.image, pipe2.x - pipe2.width / 2, pipe2.y - pipe2.height / 2, null);
		
		Font f = new Font(Font.MONOSPACED, Font.BOLD, 45);//设置实时分数字体
		g.setFont(f);
		g.setColor(Color.white);//设置绘制分数颜色
		score = bird.scoreCounter.currentScore();//获取实时分数
		g.drawString(score + "", 30, 50);//绘制实时分数
		
		g.drawImage(ground.image, ground.x, ground.y, null);//绘制地板
		
		if (gameOver) {
			//游戏结束
			g.drawImage(gameOverImg, 0, 0, null);//绘制主结束背景（GameOver字样的图片）
			try {
				//获取绘制好的计分牌图片
				scoreCurImg = ImageIO.read(new FileInputStream("image/scorebuf.png"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			g.drawImage(scoreCurImg, 82, 342, null);//绘制计分牌
			
			return;
		}
		
		if (!start) {
			//进入游戏初始界面，未开始游戏
			g.drawImage(gameStartImg, 0, 0, null);//绘制初始时的GetReady字样与游戏帮助图片
		}
		
		//游戏过程中，根据倾角旋转小鸟图片以实现飞行使鸟头随之变化的效果
		Graphics2D graphics2d = (Graphics2D) g;//Graphics2D辅助实现旋转小鸟图片
		graphics2d.rotate(bird.alpha, bird.x, bird.y);
		g.drawImage(bird.image, bird.x - bird.width / 2, bird.y - bird.height / 2, null);
		graphics2d.rotate(-bird.alpha, bird.x, bird.y);
	}

	public void start() {//重新开始游戏
		try {//重置有关参数
			speed = 60;
			start = false;
			gameOver = false;
			bird = new Bird();
			ground = new Ground();
			pipe1 = new Pipe(1);
			pipe2 = new Pipe(2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void action() throws Exception { //开始游戏， 游戏逻辑主要拼接部分
		//实现鼠标单击操作
		addMouseListener(new MouseAdapter() {//通过Adapter新建Listener
			@Override
			public void mousePressed(MouseEvent e) {
				if (gameOver) {
					//游戏结束，调用有关函数重新开始新一轮
					Gameover();//更新计分牌
					start();//开始新游戏
					return;
				}
				
				//使游戏进行
				start = true;
				//单击鼠标实现小鸟飞翔
				new Thread(new MusicThread("audio/fly.mp3")).start();//播放扇翅膀音效
				bird.flappy();//使小鸟速度改变从而飞起
			}
		});
		
		//实现按空格操作，逻辑与鼠标单击时相同
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					//只有空格键有效，键盘其余按键均无效
					if (gameOver) {
						Gameover();
						start();
						return;
					}
					
					start = true;
					new Thread(new MusicThread("audio/fly.mp3")).start();
					bird.flappy();
				}
			}
		});
		
		requestFocusInWindow();
		
		while (true) {
			
			if (!gameOver && start) {
				//游戏进行中，地板、水管后退，鸟自然下坠
				pipe1.step();
				pipe2.step();
				bird.step();
				if (bird.hit(pipe1) || bird.hit(pipe2) || bird.hit(ground)) {
					// 撞到地板或水管，小鸟死亡，游戏结束
					gameOver = true;
					new Thread(new MusicThread("audio/crash.mp3")).start();//播放撞击音效
					Gameover();//
					start = false;
				}
				if (bird.isPass(pipe1, pipe2)) {
					// 通过水管，播放得分音效，增加speed以实现休眠时间缩短，游戏难度加大
					new Thread(new MusicThread("audio/score.mp3")).start();
					speed += 7;
				}
			}
			
			if (!gameOver) {
				//游戏未结束，小鸟翅膀姿态变化
				bird.fly();
			}
			ground.step();//无论游戏状态如何，地板都在后退
			
			//线程休眠后调用repaint()以实现刷新
			Thread.sleep(1000 / speed);
			repaint();
		}
	}

	public void Gameover() {//游戏结束
		bird.scoreCounter.savaBest();//计分牌储存成绩
		ScoreCounterImg over = new ScoreCounterImg();
		over.draw(bird);//更新计分牌
	}

	public static void main(String[] args) throws Exception {//主函数
		JFrame frame = new JFrame("FlappyBird");//新建窗体命名为FlappyBird
		MyPanel game = new MyPanel();//新建游戏panel
		frame.add(game);//将游戏panel添加到窗体上
		frame.setSize(440, 673);//设置窗体大小
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);//不可更改窗体大小
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);//窗体可见
		game.action();//开始游戏
	}
}
