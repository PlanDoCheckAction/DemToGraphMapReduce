import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class MyFileOutputFormat extends FileOutputFormat<Text, Point> {

    public RecordWriter<Text, Point> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {

        FileSystem fs = FileSystem.get(job.getConfiguration());

        return new MyRecordWriter(fs);
    }
}
