import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class MyReducer extends Reducer<Text, Point, Text, Point> {

    Text K = new Text();

    @Override
    protected void reduce(Text key, Iterable<Point> values, Context context) throws IOException, InterruptedException {

        K.set(key.toString().substring(key.toString().lastIndexOf('/') + 1));

        for (Point point:values) {
            context.write(K, point);
        }
    }
}
