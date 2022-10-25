package com.example.androidapp.game.link;

//import static com.example.linkgame_beta.LinkGame.Constant.FinalValue.UNBLOCKED;

public class Kernel {

    public static final int UNBLOCKED =-1;

    /**
     * 判断两点是否可以一条线直连
     * @param LinkBoard
     * @param src
     * @param des
     * @param SealedLinkInfo
     * @return
     */
    private static boolean directLink(int[][] LinkBoard, Point src, Point des, LinkInfoList SealedLinkInfo) {
        //两点同一行的情况
        if (src.getX() == des.getX()) {
            int min = Math.min(src.getY(), des.getY());
            int max = Math.max(src.getY(), des.getY());
            for (int i = min+1; i < max; i++) {
                if (LinkBoard[src.getX()][i] != UNBLOCKED)
                    return false;
            }
            if (SealedLinkInfo != null)
                SealedLinkInfo.Seal(new LinkInfo(src, des));
            return true;
            //两点同一列的情况
        } else if (src.getY() == des.getY()) {
            int min = Math.min(src.getX(), des.getX());
            int max = Math.max(src.getX(), des.getX());
            for (int i = min+1; i < max; i++) {
                if (LinkBoard[i][src.getY()] != UNBLOCKED)
                    return false;
            }
            if (SealedLinkInfo != null)
                SealedLinkInfo.Seal(new LinkInfo(src, des));
            return true;
        }
        return false;
    }

    /**
     * 判断两点是否可以通过一个折弯相连接
     * @param LinkBoard
     * @param src
     * @param des
     * @param SealedLinkInfo
     * @return
     */
    private static boolean oneBendLink(int[][] LinkBoard, Point src, Point des, LinkInfoList SealedLinkInfo) {
        //拐点一
        Point tmp1 = new Point(des.getX(), src.getY());
        //拐点二
        Point tmp2 = new Point(src.getX(), des.getY());
        //判读两点是否能和拐点相连同时拐点是否没有阻挡
        boolean judge1 = directLink(LinkBoard, src, tmp1, null)
                && directLink(LinkBoard, tmp1, des, null)
                && LinkBoard[tmp1.getX()][tmp1.getY()] == UNBLOCKED;
        boolean judge2 = directLink(LinkBoard, src, tmp2, null)
                && directLink(LinkBoard, tmp2, des, null)
                && LinkBoard[tmp2.getX()][tmp2.getY()] == UNBLOCKED;
        if (judge1) {
            if (SealedLinkInfo != null) {
                SealedLinkInfo.Seal(new LinkInfo(src, tmp1, des));
            }
            return true;
        }
        if (judge2) {
            if (SealedLinkInfo != null) {
                SealedLinkInfo.Seal(new LinkInfo(src, tmp2, des));
            }
            return true;
        }
        return false;
    }

    /**
     * 判断两点是否可以通过两个折弯相连
     * @param LinkBoard
     * @param src
     * @param des
     * @param SealedLinkInfo
     * @return boolean
     */
    private static boolean twoBendsLink(int[][] LinkBoard, Point src, Point des, LinkInfoList SealedLinkInfo) {
        int row = LinkBoard.length;
        int col = LinkBoard[0].length;
        //寻找两个拐点
        for (int i = 0; i < col; i++)//垂直方向
        {
            Point tmp1 = new Point(src.getX(), i);
            Point tmp2 = new Point(des.getX(), i);
            if (isLinkedAndUnblocked(LinkBoard, src, des, tmp1, tmp2)) {
                if (SealedLinkInfo != null) {
                    SealedLinkInfo.Seal(new LinkInfo(src, tmp1, tmp2, des));
                }
                return true;
            }
        }
        for (int j = 0; j < row ; j++)//水平方向
        {
            Point tmp1 = new Point(j, src.getY());
            Point tmp2 = new Point(j, des.getY());
            if (isLinkedAndUnblocked(LinkBoard, src, des,tmp1, tmp2)) {
                if (SealedLinkInfo != null) {
                    SealedLinkInfo.Seal(new LinkInfo(src, tmp1, tmp2, des));
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两点和拐点能否相连且拐点无阻碍
     * @param LinkBoard
     * @param src
     * @param des
     * @param tmp1
     * @param tmp2
     * @return boolean
     */
    private static boolean isLinkedAndUnblocked(int[][] LinkBoard, Point src, Point des, Point tmp1, Point tmp2) {
        boolean isLinked = directLink(LinkBoard, src, tmp1, null)
                && directLink(LinkBoard, tmp1, tmp2, null)
                && directLink(LinkBoard, tmp2, des, null);
        boolean isUnblocked = LinkBoard[tmp1.getX()][tmp1.getY()] == UNBLOCKED
                && LinkBoard[tmp2.getX()][tmp2.getY()] == UNBLOCKED;
        return isLinked&&isUnblocked;
    }

    /**
     * 判断两点是否可以相连
     * @param LinkBoard
     * @param src
     * @param des
     * @param linkInfoList
     * @return boolean
     */
    public static boolean findLink(int[][] LinkBoard, Point src, Point des, LinkInfoList linkInfoList)
    {
        int x_s = src.getX();int y_s = src.getY();
        int x_d = des.getX();int y_d = des.getY();
        //两点必须要有相同值，且不能是无阻碍
        boolean Same = LinkBoard[x_s][y_s]==LinkBoard[x_d][y_d];
        boolean NUnblocked= !(LinkBoard[x_s][y_s]==UNBLOCKED ||LinkBoard[x_d][y_d]==UNBLOCKED);
        if(Same&&NUnblocked)
        {
            return directLink(LinkBoard,src,des, linkInfoList)||
                    oneBendLink(LinkBoard,src,des, linkInfoList)||
                    twoBendsLink(LinkBoard,src,des, linkInfoList);
        }
        return false;
    }
}



