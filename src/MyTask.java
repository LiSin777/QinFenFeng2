import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.Callable;


public class MyTask implements Callable<ByteBuffer> {
    //position用于表示线程从哪开始读取文件
    private long position;
    private FileChannel inchannel;
    private ByteBuffer buffer;

    public MyTask(FileChannel inchannel , long position) {
        this.position = position;
        this.inchannel = inchannel;
    }

    @Override
    public ByteBuffer call() throws Exception {
        //创建一个1024 * 8大小缓存区
        buffer = ByteBuffer.allocate(1024 * 8);
        inchannel.read(buffer , position);

        System.out.println(Thread.currentThread().getName() + "正在工作");
        //将读取完成的缓存区返回
        return buffer;
    }

}
