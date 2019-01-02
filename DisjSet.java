package cn.wfewater.maze;

/*
不相交集合的实现-树的实现方式
假设所有元素均为已从1到N顺序编号并且编号方法确定
根表示一个集合
 */
public class DisjSet {
    private int[] eleRoot;

    public DisjSet(int num) {
        this.eleRoot = new int[num];
        for(int j=0;j<num;j++) getEleRoot()[j] = -1;
    }
/*
find操作不要求返回任何特定的名字,只是要求当且仅当两个元素属于相同集合
作用在这这两个元素上的Find返回相同的名字
 */
    public int find(int x) {
        if (getEleRoot()[x] < 0)
            return x;
        return find(getEleRoot()[x]);

    }
/*
根元素的数组元素记录树大小的的负值
 */
    public void union(int root1,int root2) {
        if(getEleRoot()[root1] > getEleRoot()[root2]) {
            getEleRoot()[root1] = root2;
        }else {
            if (getEleRoot()[root1]==getEleRoot()[root2]) getEleRoot()[root1]--;
            getEleRoot()[root2] = root1;
        }
    }

    public int[] getEleRoot() {
        return eleRoot;
    }
}
