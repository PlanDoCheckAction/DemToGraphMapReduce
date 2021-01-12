import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.TermProgressCallback;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;

import java.io.IOException;
import java.net.URISyntaxException;

public class MyMapper extends Mapper<Point, Text, Text, Point> {

    @Override
    protected void map(Point key, Text value, Context context) throws IOException, InterruptedException {
        key.pruning();
        context.write(value,key);
    }

}
