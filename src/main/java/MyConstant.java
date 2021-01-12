public interface MyConstant {

    String inputSrc = "C:/Users/Tu/Desktop/hights.txt";//"C:/Users/Tu/Desktop/track1south10result.tif";//"/track1south10result.tif";//"hdfs://localhost:9000/track1south10result.tif";//"hdfs://hadoop01:9000/track1south11.tif";
    String outputSrc = "C:/Users/Tu/Desktop/result/";//"hdfs://hadoop01:9000/result/";
    DomainModel dmodel = DomainModel.FOUR;

    //栅格点与点之间的距离
    int pointD = 200;
    //距离的权重比例
    double weightD = 1;
    //时间的权重
    double weightT = 0;
}
