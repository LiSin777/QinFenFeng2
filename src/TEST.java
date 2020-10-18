import org.junit.Test;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.*;

import static java.util.concurrent.Executors.*;

public class TEST {
    @Test
    public void test1() throws Exception {
        //读取的文件通道
        FileChannel inChannel = new RandomAccessFile("Z:\\勤奋蜂\\2.jpg", "r").getChannel();
        //写入的文件通道
        FileChannel outChannel = new RandomAccessFile("Z:\\勤奋蜂\\8.jpg", "rw").getChannel();
        try{
            //计算需要多少个1024 * 8的缓存区，就建立几个线程
            int threadCount = (int) (inChannel.size() / (1024 * 8));
            if(inChannel.size() % (1024 * 8) != 0){
                threadCount++;
            }

            //输出线程个数
            System.out.println("线程总数：" + threadCount);
            //创建一个缓存区数组用于存每个线程结束后返回的缓存区
            ByteBuffer[] buffers = new ByteBuffer[threadCount];
            //建立一个线程池
            ExecutorService pool = newFixedThreadPool(threadCount);

            for (int i = 0; i < threadCount; i++) {
                //position是表示每个线程从position的位置开始读取文件进入缓存区
                Future<ByteBuffer> future = pool.submit(new MyTask(inChannel, i * 1024 * 8));

                //线程结束后返回一个ByteBuffer，将他按顺序存入缓存区数组中
                buffers[i] = future.get();
            }

            //两种写入文件的方式
            for (ByteBuffer buffer : buffers) {
                buffer.flip();
                outChannel.write(buffer);
            }
//            Thread.sleep(10000);
//            outChannel.write(buffers);

            pool.shutdown();
        }finally {
            inChannel.close();
            outChannel.close();
        }

    }

}

