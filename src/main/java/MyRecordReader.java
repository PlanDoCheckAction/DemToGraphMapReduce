import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
/*import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;*/

import java.io.IOException;

public class MyRecordReader extends RecordReader<Point, Text> {

    Point key = new Point();
    Text value = new Text();
    Path path = null;
    //Dataset ds = null;
    FSDataInputStream open = null;
    long len = 0;

    //X Y size
    int X, Y;
    //int X=3;
    //int Y=3;
    int[][] hights; //= new int[][]{{-9999, 1500, 1600}, {1500, 1400, 1700}, {1800, 1500, 1400}};

    int x,y;

    public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        FileSplit fileSplit = (FileSplit)split;
        path = fileSplit.getPath();

        /*//注册GDAL
        gdal.AllRegister();

        //打开数据集//改
        ds = gdal.Open("C:/Users/Tu/Desktop/track1south10result.tif", gdalconst.GA_ReadOnly);

        if (ds == null) {
            System.out.println("Can't open " + path.toString());
            System.exit(-1);
        }

        Band band = ds.GetRasterBand(1);

        X = band.getXSize();
        Y = band.getYSize();

        hights = new int[Y][X];

        for (int i = 0; i < Y; i++) {
            band.ReadRaster(0, i, X, 1, hights[i]);
        }*/

        //改

        //创建文件系统
        FileSystem fs = FileSystem.get(context.getConfiguration());
        //创建输入流
        open = fs.open(path);

        len = fileSplit.getLength();

        byte[] b = new byte[(int)len];

        open.readFully(0, b);

        String s = new String(b);

        String[] hang = s.split("\n");

        Y=hang.length;
        X=hang[0].split(" ").length;
        hights = new int[Y][X];

        for (int i=0; i<Y; i++){
            String[] hight = hang[i].split(" ");
            for (int j=0; j<X; j++){
                hights[i][j] = Integer.parseInt(hight[j]);
            }
        }

        x=y=0;

        value.set(path.toString());

    }

    public boolean nextKeyValue(){

        while (-9999 == hights[y][x]){
            if (++x>=X){
                x = 0;
                y++;
            }
            if (y>=Y)
                break;
        }

        if (y >= Y)
            return false;

        return true;
    }

    public Point getCurrentKey() throws IOException, InterruptedException {
        key.setPointX(x);
        key.setPointY(y);
        key.setHight(hights[y][x]);
        key.addAdjs(hights, x, y);

        if (++x >= X){
            x=0;
            y++;
        }

        return this.key;
    }

    public Text getCurrentValue() throws IOException, InterruptedException {
        return this.value;
    }

    public float getProgress() throws IOException, InterruptedException {
        return (x+y)/(X+Y);
    }

    public void close() throws IOException {
        //if (null != ds)
        //    ds.delete();
    }
}
