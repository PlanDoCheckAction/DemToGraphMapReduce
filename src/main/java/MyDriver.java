import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class MyDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        long startTime = System.currentTimeMillis();//获取开始时间

        Configuration conf = new Configuration();

        //conf.set("mapreduce.task.io.sort.mb","1073741825");
        //conf.set("mapreduce.map.memory.mb","2048");

        //1 获取 Job 对象
        Job job = Job.getInstance(conf);

        //2 设置 jar 存储位置
        job.setJarByClass(MyDriver.class);

        //3 关联 Map 和 Reduce 类
        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);

        //job.setNumReduceTasks(0);

        //4 设置 Mapper 阶段输出数据的 key 和 value 类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Point.class);

        //5 设置最终数据输出的 key 和 value 类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Point.class);

        // 指定自定义输入
        job.setInputFormatClass(MyFileInputFormat.class);
        //job.setOutputFormatClass(PyramidWritable.class);

        // 指定自定义输出
        job.setOutputFormatClass(MyFileOutputFormat.class);

        //6 设置输入路径和输出路径
        FileInputFormat.setInputPaths(job, new Path(MyConstant.inputSrc));

        FileOutputFormat.setOutputPath(job, new Path(MyConstant.outputSrc));

        //7 提交 job
        //job.submit();//此方法不会打印日志
        job.waitForCompletion(false);

        long endTime = System.currentTimeMillis();//获取结束时间
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");//输出程序运行时间
    }
}
