package flappybird;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 计分牌类，完成与计分有关的功能包括对记录最好成绩的Score文件的读取、写入，提供count()方法使分数+1
 * 通过public的currentScore() bestScore()返回私有的score、bestScore
 * 
 * @author 红豆冰大仙
 *
 */

public class ScoreCounter {
	private int score;//当局成绩
	private int bestScore;//最好成绩

	public ScoreCounter() {//初始化bestScore
		score= 0;
		bestScore = -1;
		try {
			loadBestScore();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadBestScore() throws Exception {// 从txt文件中读取历史最好成绩
		File file = new File("score.txt");
		if (file.exists()) {
			//若文件存在，修改bestScore
			DataInputStream din = new DataInputStream(new FileInputStream(file));//文件读取输入流
			bestScore = din.readInt();
			din.close();
		}
	}

	public void savaBest() {//保存最好成绩
		bestScore = Math.max(bestScore, score);//比较当前成绩与历史最好成绩
		try {
			//将最好成绩输出文件
			File file = new File("score.txt");
			DataOutputStream dout = new DataOutputStream(new FileOutputStream(file));//文件写入输出流
			dout.writeInt(bestScore);
			dout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void count() {//通过则加分
		score += 1;
	}

	public int currentScore() {//返回当前成绩
		return score;
	}

	public int bestScore() {//返回最好成绩
		return bestScore;
	}

}
