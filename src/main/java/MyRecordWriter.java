import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyRecordWriter extends RecordWriter<Text, Point> {

    FSDataOutputStream out = null;
    FileSystem fs;

    public MyRecordWriter(FileSystem fs) {
        this.fs = fs;
        try {
            out = fs.create(new Path(MyConstant.outputSrc + '/' + "graph.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(Text key, Point value) throws IOException, InterruptedException {

        out.write(value.toString().getBytes());
        out.write('\n');

    }

    public void close(TaskAttemptContext context) throws IOException, InterruptedException {
        if (null != out)
            out.close();
    }
}
