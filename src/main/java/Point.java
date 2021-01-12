import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Point implements Writable {

    private int pointX;
    private int pointY;
    private int hight;
    private Point[] adjs;

    public Point() {
        this.adjs = new Point[MyConstant.dmodel.getCount()];
    }

    public Point(int pointX, int pointY, int hight) {
        this.pointX = pointX;
        this.pointY = pointY;
        this.hight = hight;
    }

    private void addAdjs(int index, int pointX, int pointY, int hight){
        adjs[index] = new Point(pointX, pointY, hight);
    }

    private void domainModel4(int[][] hights, int x, int y){
        //左[0]
        if (x-1 >= 0 && hights[y][x-1] != -9999)
            addAdjs(0, x-1, y, hights[y][x-1]);
        //右[1]
        if (x+1 < hights[y].length && hights[y][x+1] != -9999)
            addAdjs(1, x+1, y, hights[y][x+1]);
        //上[2]
        if (y-1 >= 0 && hights[y-1][x] != -9999)
            addAdjs(2, x, y-1, hights[y-1][x]);
        //下[3]
        if (y+1 < hights.length && hights[y+1][x] != -9999)
            addAdjs(3, x, y+1, hights[y+1][x]);
    }

    //八邻域模型
    private void domainModel8(int[][] hights, int x, int y){

        domainModel4(hights, x, y);
        //左上
        if (x-1 >= 0 && y-1 >= 0 && hights[y-1][x-1] != -9999)
            addAdjs(4, x-1, y-1, hights[y-1][x-1]);
        //右上
        if (x+1 < hights[y].length && y-1 >= 0 && hights[y-1][x+1] != -9999)
            addAdjs(5, x+1, y-1, hights[y-1][x+1]);
        //左下
        if (x-1 >= 0 && y+1 < hights.length && hights[y+1][x-1] != -9999)
            addAdjs(6, x-1, y+1, hights[y+1][x-1]);
        //右下
        if (x+1 < hights[y].length && y+1 < hights.length && hights[y+1][x+1] != -9999)
            addAdjs(7, x+1, y+1, hights[y+1][x+1]);
    }

    //十六邻域模型
    private void domainModel16(int[][] hights, int x, int y){
        domainModel4(hights, x, y);
        domainModel8(hights, x, y);

        //左上 左
        if (x-2 >= 0 && y-1 >= 0 && hights[y-1][x-2] != -9999)
            addAdjs(8, x-2, y-1, hights[y-1][x-2]);
        //左上 上
        if (x-1 >= 0 && y-2 >= 0 && hights[y-2][x-1] != -9999)
            addAdjs(9, x-1, y-2, hights[y-2][x-1]);
        //右上 右
        if (x+2 < hights[y].length && y-1 >= 0 && hights[y-1][x+2] != -9999)
            addAdjs(10, x+2, y-1, hights[y-1][x+2]);
        //右上 上
        if (x+1 < hights[y].length && y-2 >= 0 && hights[y-2][x+1] != -9999)
            addAdjs(11, x+1, y-2, hights[y-2][x+1]);
        //左下 左
        if (x-2 >= 0 && y+1 < hights.length && hights[y+1][x-2] != -9999)
            addAdjs(12, x-2, y+1, hights[y+1][x-2]);
        //左下 下
        if (x-1 >= 0 && y+2 < hights.length && hights[y+2][x-1] != -9999)
            addAdjs(13, x-1, y+2, hights[y+2][x-1]);
        //右下 右
        if (x+2 < hights[y].length && y+1 < hights.length && hights[y+1][x+2] != -9999)
            addAdjs(14, x+2, y+1, hights[y+1][x+2]);
        //右下 下
        if (x+1 < hights[y].length && y+2 < hights.length && hights[y+2][x+1] != -9999)
            addAdjs(15, x+1, y+2, hights[y+2][x+1]);

    }

    public int getPointX() {
        return pointX;
    }

    public int getPointY() {
        return pointY;
    }

    public int getHight() {
        return hight;
    }

    public Point[] getAdjs() {
        return adjs;
    }

    public void setPointX(int pointX) {
        this.pointX = pointX;
    }

    public void setPointY(int pointY) {
        this.pointY = pointY;
    }

    public void setHight(int hight) {
        this.hight = hight;
    }

    public void addAdjs(int[][] hights, int x, int y){
        switch (MyConstant.dmodel.getCount()){
            case 4:
                domainModel4(hights, x, y);
                break;
            case 8:
                domainModel8(hights, x, y);
                break;
            case 16:
                domainModel16(hights, x, y);
                break;
        }
    }

    //剪枝,赋权重
    public void pruning(){
        switch (MyConstant.dmodel.getCount()){
            case 4:
                pruning4();
                break;
            case 8:
                pruning8();
                break;
            case 16:
                pruning16();
                break;
        }
    }

    //边权重设置
    private int putWeight(int hight, double d){

        double len = Math.sqrt(d*d+hight*hight);

        return (int)(len* MyConstant.weightD+ MyConstant.weightT);
    }

    //十六邻域剪枝
    private void pruning16() {
        int hightDifference = 0;
        double d16 = Math.sqrt(Math.pow(MyConstant.pointD, 2) + Math.pow(2* MyConstant.pointD, 2));

        pruning4();
        pruning8();

        for (int i=8; i<16; i++){

            if (null == adjs[i])
                continue;

            hightDifference = Math.abs(hight - adjs[i].getHight());
            //Hz = GL*tan, tan = hightDifference/PyramidConstant.pointD tan>=1则剪枝
            if (!(hightDifference/d16<1)){
                adjs[i] = null;
                continue;
            }

            adjs[i].setHight(putWeight(hightDifference, d16));
        }
    }

    //八邻域剪枝
    private void pruning8() {

        int hightDifference = 0;
        //Math.sqrt(Math.pow(PyramidConstant.pointD, 2) * 2) 表示斜方向上的距离
        double d8 = Math.sqrt(Math.pow(MyConstant.pointD, 2) * 2);

        pruning4();

        for (int i=4; i<8; i++){

            if (null == adjs[i])
                continue;

            hightDifference = Math.abs(hight - adjs[i].getHight());
            //Hz = GL*tan, tan = hightDifference/PyramidConstant.pointD tan>=1则剪枝
            if (!(hightDifference/d8<1)){
                adjs[i] = null;
                continue;
            }

            adjs[i].setHight(putWeight(hightDifference, d8));
        }

    }

    private void pruning4() {
        int hightDifference = 0;
        double d4 = MyConstant.pointD;

        for (int i=0; i<4; i++){

            if (null == adjs[i])
                continue;

            hightDifference = Math.abs(hight - adjs[i].getHight());
            //Hz = GL*tan, tan = hightDifference/PyramidConstant.pointD tan>=1则剪枝
            if (!(hightDifference/d4<1)){
                adjs[i] = null;
                continue;
            }

            adjs[i].setHight(putWeight(hightDifference, d4));
        }
    }

    public void write(DataOutput out) throws IOException {

        out.writeInt(pointX);
        out.writeInt(pointY);
        out.writeInt(hight);

        for (int i = 0; i<MyConstant.dmodel.getCount(); i++){
            if (null == adjs[i]) {
                out.writeInt(-1);
                continue;
            }

            out.writeInt(i);
            out.writeInt(adjs[i].getPointX());
            out.writeInt(adjs[i].getPointY());
            out.writeInt(adjs[i].getHight());
        }
    }

    public void readFields(DataInput in) throws IOException {
        this.setPointX(in.readInt());
        this.setPointY(in.readInt());
        this.setHight(in.readInt());
        this.adjs = new Point[MyConstant.dmodel.getCount()];

        for (int i=0; i<MyConstant.dmodel.getCount(); i++){
            int index = in.readInt();
            if (-1 != index){
                int x = in.readInt();
                int y = in.readInt();
                int hight = in.readInt();
                this.addAdjs(index, x, y, hight);

            }

        }
    }

    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();
        //例：X Y	2147483647 preX preY
        sb.append(pointX + " " + pointY);
        sb.append('\t');
        sb.append(Integer.MAX_VALUE + " -1 -1");

        for (int i=0; i<MyConstant.dmodel.getCount(); i++){
            if (null == adjs[i])
                continue;
            sb.append("|" + adjs[i].getPointX() + " " + adjs[i].getPointY() + " " + adjs[i].getHight() + " " + pointX + " " + pointY);
        }

        return sb.toString();
    }
}
