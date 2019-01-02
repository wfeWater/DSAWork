package cn.wfewater.maze;
/*
author:wfe
time-start:2018/12/30
time-end:2019/01/02
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Maze extends JFrame {
    private int row;
    private int col;
    private DisjSet disjSet;
    static final int WIN_WIDTH = 1000;
    static final int WIN_HEIGHT = 1000;

    public Maze(int row,int col) {
        this.row = row;
        this.col = col;
        this.setTitle("迷宫");
        this.setSize(WIN_WIDTH,WIN_HEIGHT);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setDisjSet(DisjSet disjSet) {
        this.disjSet = disjSet;
    }

    public void paint(Graphics g) {
        super.paint(g);
        List<Integer>DisList = new ArrayList<>();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,WIN_WIDTH,WIN_HEIGHT);
        g.setColor(Color.BLACK);
        final int EXTRAWIDTH = 30;
        final int cellWidth = (WIN_WIDTH-2*EXTRAWIDTH)/row;
        final int cellHeight = (WIN_HEIGHT-4*EXTRAWIDTH)/col;
        for(int i=0;i<row;i++) {
            for (int j=0;j<col;j++) {
                g.drawRect(i*cellWidth+EXTRAWIDTH,j*cellHeight+2*EXTRAWIDTH,cellWidth,cellHeight);
            }
        }
        int lastPos = (row*col)-1;

        g.setColor(Color.RED);
        g.fillRect(EXTRAWIDTH,2*EXTRAWIDTH,cellWidth,cellHeight);
        g.fillRect((lastPos%row)*cellWidth+EXTRAWIDTH,(lastPos/row)*cellHeight+2*EXTRAWIDTH,cellWidth,cellHeight);

        this.setDisjSet(new DisjSet(row*col));
        LinkedList[] myArray = new LinkedList[row*col];
        for (int i=0;i<row*col;i++) {
            LinkedList<Integer> tmp = new LinkedList<>();
            tmp.add(i);
            myArray[i] = tmp;
            tmp.clear();
        }
        Stack<Integer> path = new Stack<>();
        path.push(0);

        g.setColor(Color.WHITE);

        while (disjSet.find(0)!=disjSet.find(lastPos)) {
            //获得随机点和相邻点位置
            Random random = new Random();
            int randPos = random.nextInt(lastPos+1);
            int rowIndex = randPos%row;
            int colIndex = randPos/col;
            List<Integer> neighbors = getNeighborNum(rowIndex,colIndex);
            int randNeighbor = neighbors.get(random.nextInt(neighbors.size()));

            if(disjSet.find(randPos)==disjSet.find(randNeighbor)) {
                continue;
            }else {
                int aRoot = disjSet.find(randPos);
                int bRoot = disjSet.find(randNeighbor);
                disjSet.union(aRoot,bRoot);
                DisList.add(randPos);
                DisList.add(randNeighbor);
                //得到较大点
                int maxNum = Math.max(randPos,randNeighbor);
                int x1=0,y1=0,x2=0,y2=0;
                if(Math.abs(randPos-randNeighbor) == 1){
                    x1= x2=(maxNum% row)*cellWidth +EXTRAWIDTH;
                    y1=(maxNum/ row)*cellHeight + 2*EXTRAWIDTH;
                    y2=y1+cellHeight;
                }else {
                    y1 = y2 = (maxNum / row) * cellHeight + 2 * EXTRAWIDTH;
                    x1 = (maxNum % row) * cellWidth + EXTRAWIDTH;
                    x2 = x1 + cellWidth;
                }
                g.drawLine(x1,y1,x2,y2);
            }

    }
    //连通
        for(int i=0;i<DisList.size();i=i+2) {
            Integer j = DisList.get(i);
            Integer k = DisList.get(i+1);
            myArray[j].addLast(k);
            myArray[k].addLast(j);
        }

/*
检查i节点的联通节点
存在就放入栈中(栈中没有该元素的情况下),不存在弹出栈顶元素(删除邻接表中的数据)
重复第一步
 */
        while (true) {
            //空栈的边界条件判断
            if (path.isEmpty()) {
                System.out.println("空栈,无路径");
                break;
            }
            //结束的边界条件判断
            if (path.peek() == (row * col - 1)) {
                System.out.println("破解成功");
                break;
            }
            if (myArray[path.peek()].size() == 1 && path.peek() != 0) {
                Integer tmp = path.pop();
                if (path.isEmpty()) {
                    System.out.println("空栈,没有路径");
                    break;
                }else {
                    myArray[path.peek()].remove(tmp);
                    myArray[tmp].remove(path.peek());
                }
            }else {
                if (path.search(myArray[path.peek()].get(0))!=-1) {
                    if (myArray[path.peek()].size() > 1) {
                        Iterator<Integer> out = myArray[path.peek()].listIterator();
                        while ((out.hasNext())) {
                            int n = out.next();
                            if (!path.contains(n)) {
                                path.push(n);
                            }
                        }
                    }
                }else  {
                    Integer arrayMem = Integer.parseInt(myArray[path.peek()].get(0).toString());
                    path.push(arrayMem);
                }
            }

        }

        g.setColor(Color.BLUE);
        while (!path.isEmpty()) {
            int out = path.pop();
            g.fillOval((out%row)*cellWidth+cellWidth/4+EXTRAWIDTH,(out/row)*cellHeight+cellHeight/4+2*EXTRAWIDTH,
                    cellWidth/2,cellHeight/2);
        }
        path.clear();
    }
    //计算位置
    public int getNum(int x,int y) {
        return y * col + x;
    }
    //判断是否在范围内
    public boolean isIn(int x,int y) {
        if (x<0||y<0) return false;
        return x<row && y<col;
    }
    //获得邻接点位置
    public List<Integer> getNeighborNum(int rowIndex,int colIndex) {
        List<Integer> ln = new ArrayList<Integer>(4);
        //Left,Right,Down,Up
        if(isIn(rowIndex+1,colIndex)) {
            ln.add(getNum(rowIndex+1,colIndex));
        }
        if(isIn(rowIndex,colIndex+1)) {
            ln.add(getNum(rowIndex,colIndex+1));
        }
        if(isIn(rowIndex-1,colIndex)) {
            ln.add(getNum(rowIndex-1,colIndex));
        }
        if ((isIn(rowIndex,colIndex-1))) {
            ln.add(getNum(rowIndex,colIndex-1));
        }
        return ln;
    }
    public static void main(String[] args) {
        int rowCount = 20;
        int colCount = 20;
        Maze maze = new Maze(rowCount,colCount);
    }
}
