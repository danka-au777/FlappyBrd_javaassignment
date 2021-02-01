package flappybird;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.*;

/**
 * 音效线程类，使用JLayer包的player类作为mp3播放器，
 * 重写run()方法，在调用public的线程类构造函数后播放音效（只播一遍）
 * @author 红豆冰大仙
 *
 */

public class MusicThread extends Thread {
	private Player player;//播放器
	private File bgm;//音乐文件

	public MusicThread(String filepath) {//有3种音效，调用时输入各音效文件路径
		this.bgm = new File(filepath);
	}

	@Override
	public void run() {
		super.run();
		try {
			BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(bgm));
			player = new Player(buffer);//通过BufferredInputStream构建播放器
			player.play();//播放
		} catch (FileNotFoundException | JavaLayerException e) {
			e.printStackTrace();
		}
	}
}
